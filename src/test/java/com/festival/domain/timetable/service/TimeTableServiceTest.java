package com.festival.domain.timetable.service;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.member.fixture.MemberFixture;
import com.festival.domain.member.service.MemberService;
import com.festival.domain.timetable.dto.TimeTableReq;
import com.festival.domain.timetable.model.TimeTable;
import com.festival.domain.timetable.repository.TimeTableRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.festival.domain.timetable.fixture.TimeTableFixture.TERMINATED_TIMETABLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TimeTableServiceTest {

    @InjectMocks
    private TimeTableService timeTableService;

    @Mock
    private TimeTableRepository timeTableRepository;

    @Mock
    private MemberService memberService;

    @DisplayName("타임테이블을 생성한 후 timeTableId를 반환한다.")
    @Test
    void createTimeTable() throws IOException {
        //given
        TimeTableReq timeTableReq = getTimeTableCreateReq();
        given(timeTableRepository.save(any(TimeTable.class)))
                .willReturn(getTimeTable());

        //when
        Long timeTableId = timeTableService.createTimeTable(timeTableReq);

        //then
        Assertions.assertThat(timeTableId).isEqualTo(1L);
    }

    @DisplayName("타임테이블을 업데이트하면 timeTableId를 반환한다.")
    @Test
    void updateTimeTable1(){
        // given
        TimeTableReq timeTableUpdateReqReq = getTimeTableUpdateReq();

        given(timeTableRepository.findById(1L))
                .willReturn(Optional.of(getTimeTable()));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when
        Long timeTableId = timeTableService.updateTimeTable(1L, timeTableUpdateReqReq);

        //then
        Assertions.assertThat(timeTableId).isEqualTo(1L);
    }

    @DisplayName("권한 없는 사용자가 타임테이블을 업데이트하면 ForbiddenException을 반환한다.")
    @Test
    void updateTimeTable2(){
        // given
        TimeTableReq timeTableUpdateReqReq = getTimeTableUpdateReq();

        given(timeTableRepository.findById(1L))
                .willReturn(Optional.of(getTimeTable()));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER1);

        //when & then
        assertThatThrownBy(() -> timeTableService.updateTimeTable(1L, timeTableUpdateReqReq))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_UPDATE);
    }

    @DisplayName("존재하지 않는 타임테이블을 업데이트하면 NotFoundException을 반환한다.")
    @Test
    void updateNonExistsTimeTable()  {
        TimeTableReq timeTableUpdateReqReq = getTimeTableUpdateReq();

        given(timeTableRepository.findById(1L))
                .willReturn(Optional.empty());


        //when & then
        assertThatThrownBy(() -> timeTableService.updateTimeTable(1L, timeTableUpdateReqReq))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_TIMETABLE);
    }

    @DisplayName("이미 삭제된 타임테이블을 업데이트하면 AlreadyDeletedException을 반환한다.")
    @Test
    void updateAlreadyExistsTimeTable()  {
        TimeTableReq timeTableUpdateReqReq = getTimeTableUpdateReq();

        given(timeTableRepository.findById(1L))
                .willReturn(Optional.of(TERMINATED_TIMETABLE));

        //when & then
        assertThatThrownBy(() -> timeTableService.updateTimeTable(1L, timeTableUpdateReqReq))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);
    }

    @DisplayName("타임테이블을 삭제하면 timeTableId를 반환한다.")
    @Test
    void deleteTimeTable1(){
        //given
        given(timeTableRepository.findById(1L))
                .willReturn(Optional.of(getTimeTable()));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when & then
        timeTableService.deleteTimeTable(1L);
    }

    @DisplayName("권한 없는 사용자가 타임테이블을 삭제 ForbiddenException을 반환한다.")
    @Test
    void deleteTimeTable2(){
        //given
        given(timeTableRepository.findById(1L))
                .willReturn(Optional.of(getTimeTable()));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER1);

        //when & then
        assertThatThrownBy(() -> timeTableService.deleteTimeTable(1L))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_DELETE);
    }

    @DisplayName("존재하지 않는 타임테이블을 삭제하면 NotFoundException을 반환한다.")
    @Test
    void deleteNonExistsTimeTable()  {
        //given
        given(timeTableRepository.findById(1L))
                .willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> timeTableService.deleteTimeTable(1L))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_TIMETABLE);
    }

    @DisplayName("이미 삭제된 타임테이블을 업데이트하면 AlreadyDeletedException을 반환한다.")
    @Test
    void deleteAlreadyExistsTimeTable()  {
        //given
        given(timeTableRepository.findById(1L))
                .willReturn(Optional.of(TERMINATED_TIMETABLE));

        //when & then
        assertThatThrownBy(() -> timeTableService.deleteTimeTable(1L))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);
    }

    private TimeTable getTimeTable(){
        LocalDateTime now = LocalDateTime.now();
        TimeTable timeTable = TimeTable.builder()
                .title("동아리 공연")
                .startTime(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute()))
                .endTime(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 2, now.getMinute()))
                .build();
        ReflectionTestUtils.setField(timeTable, "id", 1L);
        return timeTable;
    }

    private TimeTableReq getTimeTableCreateReq() {
        LocalDateTime now = LocalDateTime.now();
        return TimeTableReq.builder()
                .title("동아리 공연")
                .startTime(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute()))
                .endTime(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 2, now.getMinute()))
                .status("OPERATE")
                .build();
    }
    private TimeTableReq getTimeTableUpdateReq() {
        LocalDateTime now = LocalDateTime.now();
        return TimeTableReq.builder()
                .title("동아리 공연")
                .startTime(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute()))
                .endTime(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 2, now.getMinute()))
                .status("TERMINATE")
                .build();
    }
}