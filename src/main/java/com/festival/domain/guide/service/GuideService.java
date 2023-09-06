package com.festival.domain.guide.service;

import com.festival.common.base.OperateStatus;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.redis.RedisService;
import com.festival.common.util.SecurityUtils;
import com.festival.domain.guide.dto.GuidePageRes;
import com.festival.domain.guide.dto.GuideReq;
import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.guide.model.Guide;
import com.festival.domain.guide.repository.GuideRepository;
import com.festival.domain.guide.repository.vo.GuideSearchCond;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.festival.common.exception.ErrorCode.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GuideService {

    private final GuideRepository guideRepository;

    private final MemberService memberService;
    private final ImageService imageService;

    private final RedisService redisService;

    @Transactional
    public Long createGuide(GuideReq guideReq) {
        Guide guide = Guide.of(guideReq);
        guide.setImage(imageService.createImage(guideReq.getMainFile(), guideReq.getSubFiles(), guideReq.getType()));
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
        settingImage(guideReq, guide);
        return guide.getId();
    }

    @Transactional
    public void deleteGuide(Long id) {
        Guide guide = checkingDeletedStatus(guideRepository.findById(id));

        if (!SecurityUtils.checkingAdminRole(memberService.getAuthenticationMember().getMemberRoles())) {
            throw new ForbiddenException(FORBIDDEN_DELETE);
        }

        guide.changeStatus(OperateStatus.TERMINATE);
    }

    public GuideRes getGuide(Long id, String ipAddress){
        Guide guide = guideRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_GUIDE));
        if(!redisService.isDuplicateAccess(ipAddress, guide.getId())) {
            redisService.increaseRedisViewCount("Guide_Id_" + guide.getId());
        }
        return GuideRes.of(guide);
    }

    public GuidePageRes getGuideList(String status, Pageable pageable) {
        GuideSearchCond guideSearchCond = GuideSearchCond.builder()
                .status(status)
                .pageable(pageable)
                .build();
        return guideRepository.getList(guideSearchCond);
    }

    private void settingImage(GuideReq guideReq, Guide guide) {
        if (guide.getImage() != null) {
            imageService.deleteImage(guide.getImage());
        }
        if (guideReq.getMainFile() != null || guideReq.getSubFiles() != null) {
            guide.setImage(imageService.createImage(guideReq.getMainFile(), guideReq.getSubFiles(), guideReq.getType()));
        }
    }

    private Guide checkingDeletedStatus(Optional<Guide> guide) {
        if (guide.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_GUIDE);
        }
        if (guide.get().getStatus().equals(OperateStatus.TERMINATE)) {
            throw new AlreadyDeleteException(ALREADY_DELETED);
        }
        return guide.get();
    }
}
