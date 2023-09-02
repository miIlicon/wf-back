package com.festival.domain.booth.service;

import com.festival.common.base.OperateStatus;
import com.festival.common.exception.custom_exception.BadRequestException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.util.SecurityUtils;
import com.festival.domain.booth.controller.dto.BoothListReq;
import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.repository.BoothRepository;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.festival.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BoothService {

    private final BoothRepository boothRepository;

    private final ImageService imageService;

    public Long createBooth(BoothReq boothReq) {
        Booth booth = Booth.of(boothReq);
        booth.setImage(imageService.createImage(boothReq.getMainFile(), boothReq.getSubFiles(), boothReq.getType()));

        return boothRepository.save(booth).getId();
    }

    public Long updateBooth(BoothReq boothReq, Long id) {
        Booth booth = checkingDeletedStatus(boothRepository.findById(id));

        if (!SecurityUtils.checkingRole(booth.getMember(), memberService.getAuthenticationMember())) {
            throw new ForbiddenException(FORBIDDEN_UPDATE);
        }

        /**
         * @Todo
         *  delete로직 작성해야함
         */
        booth.update(boothReq);
        booth.setImage(imageService.createImage(boothReq.getMainFile(), boothReq.getSubFiles(), boothReq.getType()));
        return id;
    }

    public void deleteBooth(Long id) {
        Booth booth = checkingDeletedStatus(boothRepository.findById(id));
        if (!SecurityUtils.checkingRole(booth.getMember(), memberService.getAuthenticationMember())) {
            throw new ForbiddenException(FORBIDDEN_DELETE);
        }
        booth.changeStatus(OperateStatus.TERMINATE);
    }

    public BoothRes getBooth(Long id) {
        return BoothRes.of(boothRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_BOOTH)));
    }

    public List<BoothRes> getBoothList(BoothListReq boothListReq, Pageable pageable) {
        List<Booth> list = boothRepository.getList(BoothListSearchCond.builder()
                .status(boothListReq.getStatus())
                .type(boothListReq.getType()).build(), pageable);
        return list.stream().map(BoothRes::of).collect(Collectors.toList());
    }

    private Booth checkingDeletedStatus(Optional<Booth> booth) {
        if (booth.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_BOOTH);
        }
        if (booth.get().getStatus() == OperateStatus.TERMINATE) {
            throw new BadRequestException(ALREADY_DELETED);
        }
        return booth.get();
    }

}
