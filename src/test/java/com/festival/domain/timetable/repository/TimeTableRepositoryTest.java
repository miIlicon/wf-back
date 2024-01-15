package com.festival.domain.timetable.repository;

import com.festival.domain.member.model.Member;
import com.festival.domain.member.model.MemberRole;
import com.festival.domain.member.repository.MemberRepository;
import com.festival.domain.timetable.dto.TimeTableReq;
import com.festival.domain.timetable.dto.TimeTableRes;
import com.festival.domain.timetable.dto.TimeTableSearchCond;
import com.festival.domain.timetable.model.TimeTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class TimeTableRepositoryTest {

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member savedMember;

    @BeforeEach
    void setUp() {
        savedMember = memberRepository.save(Member.builder()
                .username("testMember")
                .password("testPassword")
                .memberRole(MemberRole.ADMIN)
                .build());
    }

    @DisplayName("지정 기간동안의 시간표 게시물들을 조회한다.")
    @Test
    void getList() throws Exception {
        //given
        LocalDateTime startTime = LocalDateTime.of(2023, 11, 13, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 11, 14, 0, 0, 0);
        TimeTable timeTable1 = createTimeTable("hello1", startTime.plusDays(1), endTime.plusDays(1));
        TimeTable timeTable2 = createTimeTable("hello2", startTime.plusDays(2), endTime.plusDays(2));
        TimeTable timeTable3 = createTimeTable("hello3", startTime.plusDays(3), endTime.plusDays(3));
        timeTableRepository.saveAll(List.of(timeTable1, timeTable2, timeTable3));

        //when
        LocalDateTime startDateTime = LocalDateTime.of(2023, 11, 13, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 11, 15, 0, 0, 0);
        List<TimeTableRes> timeTableRes = timeTableRepository.getList(TimeTableSearchCond.builder()
                .startTime(startDateTime)
                .endTime(endDateTime)
                .build());

        //then
        assertThat(timeTableRes).hasSize(1);
        assertThat(timeTableRes.get(0).getTitle()).isEqualTo("hello1");
    }

    private TimeTable createTimeTable(String title, LocalDateTime startTime, LocalDateTime endTime) {
        TimeTable timeTable = TimeTable.of(TimeTableReq.builder()
                .title(title)
                .startTime(startTime)
                .endTime(endTime)
                .build());
        timeTable.connectMember(savedMember);
        return timeTable;
    }
}