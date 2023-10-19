package com.festival.domain.guide.service;

import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.redis.RedisService;
import com.festival.common.util.SecurityUtils;
import com.festival.domain.guide.dto.GuideListReq;
import com.festival.domain.guide.dto.GuidePageRes;
import com.festival.domain.guide.dto.GuideReq;
import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.guide.model.Guide;
import com.festival.domain.guide.repository.GuideRepository;
import com.festival.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.festival.common.exception.ErrorCode.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GuideService {

    private final GuideRepository guideRepository;

    private final MemberService memberService;
    private final RedisService redisService;

    @Transactional
    public Long createGuide(GuideReq guideReq) {
        Guide guide = Guide.of(guideReq);
        guide.connectMember(memberService.getAuthenticationMember());
        return guideRepository.save(guide).getId();
    }

    @Transactional
    public Long updateGuide(Long id, GuideReq guideReq) {
        Guide guide = checkingDeletedStatus(guideRepository.findById(id));

        if (!SecurityUtils.checkingAdminRole(memberService.getAuthenticationMember().getMemberRoles())) {
            throw new ForbiddenException(FORBIDDEN_UPDATE);
        }

        guide.update(guideReq);
        return guide.getId();
    }

    @Transactional
    public void deleteGuide(Long id) {
        Guide guide = checkingDeletedStatus(guideRepository.findById(id));

        if (!SecurityUtils.checkingAdminRole(memberService.getAuthenticationMember().getMemberRoles())) {
            throw new ForbiddenException(FORBIDDEN_DELETE);
        }

        guide.deletedGuide();
    }

    public GuideRes getGuide(Long id, String ipAddress){
        Guide guide = checkingDeletedStatus(guideRepository.findById(id));
        if(!redisService.isDuplicateAccess(ipAddress, "Guide_" + guide.getId())) {
            redisService.increaseRedisViewCount("viewCount_" + "Guide_" + guide.getId());
            redisService.setDuplicateAccess(ipAddress, "Guide_" + guide.getId());
        }

        return GuideRes.of(guide);
    }

    public GuidePageRes getGuideList(GuideListReq guideListReq) {

        return guideRepository.getList(PageRequest.of(guideListReq.getPage(), guideListReq.getSize()));
    }

    @Transactional
    public void increaseGuideViewCount(Long id, Long viewCount) {
        Guide guide = guideRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_GUIDE));
        guide.increaseViewCount(viewCount);
    }

    @Transactional
    public void decreaseGuideViewCount(Long id, Long viewCount) {
        Guide guide = guideRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_GUIDE));
        guide.decreaseViewCount(viewCount);
    }

    private Guide checkingDeletedStatus(Optional<Guide> guide) {
        if (guide.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_GUIDE);
        }
        if (guide.get().isDeleted()) {
            throw new AlreadyDeleteException(ALREADY_DELETED);
        }
        return guide.get();
    }
}
