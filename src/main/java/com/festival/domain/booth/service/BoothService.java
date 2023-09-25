package com.festival.domain.booth.service;

import com.festival.common.base.OperateStatus;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.redis.RedisService;
import com.festival.common.util.SecurityUtils;
import com.festival.domain.booth.controller.dto.*;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.repository.BoothRepository;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.festival.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BoothService {

    private final BoothRepository boothRepository;

    private final RedisService redisService;
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

    public BoothRes getBooth(Long id, String ipAddress) {
        Booth booth = boothRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_BOOTH));
        if(redisService.isDuplicateAccess(ipAddress, "Booth_" + booth.getId())) {
            redisService.increaseRedisViewCount("Booth_Id_" + booth.getId());
        }
        return BoothRes.of(booth);
    }

    @Transactional
    public BoothRes getBoothQuery(Long id, String ipAddress) {
        Booth booth = boothRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_BOOTH));

        booth.increaseViewCount(1L);

        return BoothRes.of(booth);
    }

    @Transactional(readOnly = true)
    public BoothPageRes getBoothList(BoothListReq boothListReq) {
        return boothRepository.getList(BoothListSearchCond.builder()
                .status(boothListReq.getStatus())
                .type(boothListReq.getType())
                .pageable(PageRequest.of(boothListReq.getPage() ,boothListReq.getSize()))
                .build());

    }

    @Transactional(readOnly = true)
    public List<BoothSearchRes> searchBoothList(String keyword) {
        return boothRepository.searchBoothList(keyword);
    }

    public void increaseBoothViewCount(Long id, Long viewCount) {
        Booth booth = boothRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_BOOTH));
        booth.increaseViewCount(viewCount);
    }

    public void decreaseBoothViewCount(Long id, Long viewCount) {
        Booth booth = boothRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_BOOTH));
        booth.decreaseViewCount(viewCount);
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

    public BoothRes getBoothByTitle(String title, String remoteAddr) {
        Booth booth = boothRepository.getBoothByTitle(title);
        if(redisService.isDuplicateAccess(remoteAddr, "Booth_" + booth.getId())) {
            redisService.increaseRedisViewCount("Booth_Id_" + booth.getId());
        }
        return BoothRes.of(booth);
    }

    public Long getBoothRedis(String remoteAddr) {
        return redisService.getData("Booth_Id_1");

    }
}
