package com.festival.domain.booth.service;

import com.festival.common.base.OperateStatus;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.BadRequestException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.util.SecurityUtils;
import com.festival.domain.booth.controller.dto.BoothListReq;
import com.festival.domain.booth.controller.dto.BoothPageRes;
import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.repository.BoothRepository;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final MemberService memberService;

    public Long createBooth(BoothReq boothReq) {
        Booth booth = Booth.of(boothReq);
        booth.setImage(imageService.createImage(boothReq.getMainFile(), boothReq.getSubFiles(), boothReq.getType()));
        booth.connectMember(memberService.getAuthenticationMember());
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

    @Transactional(readOnly = true)
    public BoothRes getBooth(Long id, String ip) {
        return BoothRes.of(boothRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_BOOTH)));
    }

    @Transactional(readOnly = true)
    public BoothPageRes getBoothList(BoothListReq boothListReq, Pageable pageable) {
        return boothRepository.getList(BoothListSearchCond.builder()
                .status(boothListReq.getStatus())
                .type(boothListReq.getType())
                .pageable(pageable)
                .build());

    }

    private Booth checkingDeletedStatus(Optional<Booth> booth) {
        if (booth.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_BOOTH);
        }
        if (booth.get().getStatus() == OperateStatus.TERMINATE) {
            throw new AlreadyDeleteException(ALREADY_DELETED);
        }
        return booth.get();
    }

}
