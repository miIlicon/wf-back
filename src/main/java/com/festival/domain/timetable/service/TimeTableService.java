package com.festival.domain.timetable.service;

import com.festival.common.base.OperateStatus;
import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.util.SecurityUtils;
import com.festival.domain.member.service.MemberService;
import com.festival.domain.timetable.dto.TimeTableDateReq;
import com.festival.domain.timetable.dto.TimeTableReq;
import com.festival.domain.timetable.dto.TimeTableRes;
import com.festival.domain.timetable.dto.TimeTableSearchCond;
import com.festival.domain.timetable.model.TimeTable;
import com.festival.domain.timetable.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.festival.common.exception.ErrorCode.ALREADY_DELETED;
import static com.festival.common.exception.ErrorCode.NOT_FOUND_TIMETABLE;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;

    private final MemberService memberService;

    @Transactional
    public Long createTimeTable(TimeTableReq timeTableReq) {
        TimeTable timeTable = TimeTable.of(timeTableReq);
        timeTable.connectMember(memberService.getAuthenticationMember());
        TimeTable savedTimeTable = timeTableRepository.save(timeTable);
        return savedTimeTable.getId();
    }

    @Transactional
    public Long updateTimeTable(Long timeTableId, TimeTableReq timeTableReq) {
        TimeTable timeTable = checkingDeletedStatus(timeTableRepository.findById(timeTableId));

        if(!SecurityUtils.checkingAdminRole(memberService.getAuthenticationMember().getMemberRoles())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_UPDATE);
        }
        timeTable.update(timeTableReq);
        return timeTable.getId();
    }

    @Transactional
    public void deleteTimeTable(Long id) {
        TimeTable timeTable = checkingDeletedStatus(timeTableRepository.findById(id));
        if(!SecurityUtils.checkingAdminRole(memberService.getAuthenticationMember().getMemberRoles())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_DELETE);
        }
        timeTable.changeStatus(OperateStatus.TERMINATE);
    }

    public List<TimeTableRes> getTimeTableList(TimeTableDateReq timeTableDateReq) {
        TimeTableSearchCond timeTableSearchCond =TimeTableSearchCond.builder()
                .startTime(timeTableDateReq.getStartTime())
                .endTime(timeTableDateReq.getEndTime())
                .status(timeTableDateReq.getStatus()).build();


        return timeTableRepository.getList(timeTableSearchCond);
    }

    private TimeTable checkingDeletedStatus(Optional<TimeTable> timeTable) {
        if (timeTable.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_TIMETABLE);
        }

        if (timeTable.get().getStatus() == OperateStatus.TERMINATE) {
            throw new AlreadyDeleteException(ALREADY_DELETED);
        }
        return timeTable.get();
    }

}
