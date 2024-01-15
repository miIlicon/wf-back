package com.festival.domain.guide.notice.service;

import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.util.SecurityUtils;
import com.festival.domain.guide.notice.dto.NoticeListReq;
import com.festival.domain.guide.notice.dto.NoticePageRes;
import com.festival.domain.guide.notice.dto.NoticeReq;
import com.festival.domain.guide.notice.dto.NoticeRes;
import com.festival.domain.guide.notice.model.Notice;
import com.festival.domain.guide.notice.repository.NoticeRepository;
import com.festival.domain.member.service.MemberService;
import com.festival.domain.viewcount.util.ViewCountUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.festival.common.exception.ErrorCode.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final MemberService memberService;
    private final ViewCountUtil viewCountUtil;

    @Transactional
    public Long createGuide(NoticeReq noticeReq) {
        Notice notice = Notice.of(noticeReq);
        notice.connectMember(memberService.getAuthenticationMember());
        return noticeRepository.save(notice).getId();
    }

    @Transactional
    public Long updateGuide(Long id, NoticeReq noticeReq) {
        Notice notice = checkingDeletedStatus(noticeRepository.findById(id));

        if (!SecurityUtils.checkingAdminRole(memberService.getAuthenticationMember().getRole())) {
            throw new ForbiddenException(FORBIDDEN_UPDATE);
        }

        notice.update(noticeReq);
        return notice.getId();
    }

    @Transactional
    public void deleteGuide(Long id) {
        Notice notice = checkingDeletedStatus(noticeRepository.findById(id));

        if (!SecurityUtils.checkingAdminRole(memberService.getAuthenticationMember().getRole())) {
            throw new ForbiddenException(FORBIDDEN_DELETE);
        }

        notice.deletedGuide();
    }

    public NoticeRes getGuide(Long id, String ipAddress){
        Notice notice = checkingDeletedStatus(noticeRepository.findById(id));
        if(!viewCountUtil.isDuplicatedAccess(ipAddress, "Guide_" + notice.getId())) {
            viewCountUtil.increaseData("viewCount_" + "Guide_" + notice.getId());
            viewCountUtil.setDuplicateAccess(ipAddress, "Guide_" + notice.getId());
        }

        return NoticeRes.of(notice);
    }

    public NoticePageRes getGuideList(NoticeListReq noticeListReq) {
        return noticeRepository.getList(PageRequest.of(noticeListReq.getPage(), noticeListReq.getSize()));
    }

    @Transactional
    public void increaseGuideViewCount(Long id, Long viewCount) {
        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_GUIDE));
        notice.increaseViewCount(viewCount);
    }

    private Notice checkingDeletedStatus(Optional<Notice> guide) {
        if (guide.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_GUIDE);
        }
        if (guide.get().isDeleted()) {
            throw new AlreadyDeleteException(ALREADY_DELETED);
        }
        return guide.get();
    }
}
