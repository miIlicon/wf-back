package com.festival.domain.timetable.controller;

import com.festival.common.base.OperateStatus;
import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.member.model.Member;
import com.festival.domain.program.dto.ProgramRes;
import com.festival.domain.timetable.dto.TimeTableReq;
import com.festival.domain.timetable.dto.TimeTableRes;
import com.festival.domain.timetable.model.TimeTable;
import com.festival.domain.timetable.repository.TimeTableRepository;
import com.festival.domain.util.ControllerTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;

import static com.festival.domain.member.model.MemberRole.ADMIN;
import static com.festival.domain.member.model.MemberRole.MANAGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TimeTableControllerTest extends ControllerTestSupport {

    @Autowired
    private TimeTableRepository timeTableRepository;

    private Member member;

    private Member differentMember;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("testUser")
                .password("12345")
                .memberRole(ADMIN)
                .build();
        memberRepository.saveAndFlush(member);

        differentMember = Member.builder()
                .username("differentUser")
                .password("12345")
                .memberRole(MANAGER)
                .build();
        memberRepository.saveAndFlush(differentMember);
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("시간표 객체를 생성한다.")
    @Test
    void createTimeTable() throws Exception {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.of(2023, 9, 1, 11, 41, 0);

        TimeTableReq timeTableReq = createTimeTableReqCount(registeredDateTime, 1);

        //when
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/v2/timetable")
                                .contentType(APPLICATION_FORM_URLENCODED)
                                .param("title", timeTableReq.getTitle())
                                .param("startTime", timeTableReq.getStartTime().toString())
                                .param("endTime", timeTableReq.getEndTime().toString())
                                .param("status", timeTableReq.getStatus())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
                .andReturn();

        //then
        Long timeTableId = Long.parseLong(mvcResult.getResponse().getContentAsString());
        TimeTable findTimeTable = timeTableRepository.findById(timeTableId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TIMETABLE));
        assertThat(findTimeTable).isNotNull()
                .extracting("title", "startTime", "endTime", "status")
                .containsExactly("testTitle1", registeredDateTime, registeredDateTime.plusHours(1), OperateStatus.OPERATE);
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("시간표 객체를 수정한다.")
    @Test
    void updateTimeTable() throws Exception {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.of(2023, 9, 1, 11, 41, 0);
        LocalDateTime updatedDateTime = LocalDateTime.of(2023, 9, 1, 12, 41, 0);

        TimeTableReq timeTableReq = createTimeTableReqCount(registeredDateTime, 1);
        TimeTable timeTable = TimeTable.of(timeTableReq);
        timeTable.connectMember(member);
        TimeTable savedTimeTable = timeTableRepository.saveAndFlush(timeTable);

        //when
        MvcResult mvcResult = mockMvc.perform(
                        put("/api/v2/timetable/{timeTableId}", savedTimeTable.getId())
                                .contentType(APPLICATION_FORM_URLENCODED)
                                .param("title", "updateTitle")
                                .param("startTime", updatedDateTime.toString())
                                .param("endTime", updatedDateTime.plusHours(1).toString())
                                .param("status", timeTableReq.getStatus())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(savedTimeTable.getId().toString()))
                .andReturn();

        //then
        Long timeTableId = Long.parseLong(mvcResult.getResponse().getContentAsString());
        TimeTable findTimeTable = timeTableRepository.findById(timeTableId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TIMETABLE));
        assertThat(findTimeTable).isNotNull()
                .extracting("title", "startTime", "endTime", "status")
                .containsExactly("updateTitle", updatedDateTime, updatedDateTime.plusHours(1), OperateStatus.OPERATE);
    }

    @WithMockUser(username = "differentUser", roles = "MANAGER")
    @DisplayName("권한이 없는 시간표 객체는 수정할 수 없다.")
    @Test
    void updateTimeTableNotMine() throws Exception {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.of(2023, 9, 1, 11, 41, 0);
        LocalDateTime updatedDateTime = LocalDateTime.of(2023, 9, 1, 12, 41, 0);

        TimeTableReq timeTableReq = createTimeTableReqCount(registeredDateTime, 1);
        TimeTable timeTable = TimeTable.of(timeTableReq);
        timeTable.connectMember(member);
        TimeTable savedTimeTable = timeTableRepository.saveAndFlush(timeTable);

        //when //then
        mockMvc.perform(
                        put("/api/v2/timetable/{timeTableId}", savedTimeTable.getId())
                                .contentType(APPLICATION_FORM_URLENCODED)
                                .param("title", "updateTitle")
                                .param("startTime", updatedDateTime.toString())
                                .param("endTime", updatedDateTime.plusHours(1).toString())
                                .param("status", timeTableReq.getStatus())
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("존재하지 않는 시간표 객체는 수정할 수 없다.")
    @Test
    void updateTimeTableNotFound() throws Exception {
        //when //then
        mockMvc.perform(
                        put("/api/v2/timetable/{timeTableId}", 1L)
                                .contentType(APPLICATION_FORM_URLENCODED)
                                .param("title", "updateTitle")
                                .param("startTime", LocalDateTime.of(2023, 9, 1, 12, 41, 0).toString())
                                .param("endTime", LocalDateTime.of(2023, 9, 1, 13, 41, 0).toString())
                                .param("status", "OPERATE")
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("시간표 객체를 삭제한다.")
    @Test
    void deleteTimeTable() throws Exception {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.of(2023, 9, 1, 11, 41, 0);

        TimeTableReq timeTableReq = createTimeTableReqCount(registeredDateTime, 1);
        TimeTable timeTable = TimeTable.of(timeTableReq);
        timeTable.connectMember(member);
        TimeTable savedTimeTable = timeTableRepository.saveAndFlush(timeTable);

        //when
        mockMvc.perform(
                        delete("/api/v2/timetable/" + savedTimeTable.getId())
                )
                .andDo(print())
                .andExpect(status().isOk());

        //then
        TimeTable findTimeTable = timeTableRepository.findById(savedTimeTable.getId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TIMETABLE));
        assertThat(findTimeTable.getStatus()).isEqualTo(OperateStatus.TERMINATE);
    }

    @WithMockUser(username = "differentUser", roles = "MANAGER")
    @DisplayName("권한이 없는 시간표 객체는 삭제할 수 없다.")
    @Test
    void deleteTimeTableNotMine() throws Exception {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.of(2023, 9, 1, 11, 41, 0);

        TimeTableReq timeTableReq = createTimeTableReqCount(registeredDateTime, 1);
        TimeTable timeTable = TimeTable.of(timeTableReq);
        timeTable.connectMember(member);
        TimeTable savedTimeTable = timeTableRepository.saveAndFlush(timeTable);

        //when //then
        mockMvc.perform(
                        delete("/api/v2/timetable/" + savedTimeTable.getId())
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("존재하지 않는 시간표 객체는 삭제할 수 없다.")
    @Test
    void deleteTimeTableNotFound() throws Exception {
        //when //then
        mockMvc.perform(
                        delete("/api/v2/timetable/" + 1L)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("시간표 객체를 목록조회한다. (페이징)")
    @Test
    void getTimeTableList() throws Exception {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.of(2023, 9, 1, 11, 41, 0);

        TimeTableReq timeTableReq1 = createTimeTableReqCount(registeredDateTime, 1);
        TimeTableReq timeTableReq2 = createTimeTableReqCount(registeredDateTime, 2);
        TimeTableReq timeTableReq3 = createTimeTableReqCount(registeredDateTime, 3);
        TimeTableReq timeTableReq4 = createTimeTableReqCount(registeredDateTime, 4);
        TimeTableReq timeTableReq5 = createTimeTableReqCount(registeredDateTime, 5);
        TimeTableReq timeTableReq6 = createTimeTableReqCount(registeredDateTime, 6);

        TimeTable timeTable1 = TimeTable.of(timeTableReq1);
        timeTable1.connectMember(member);
        TimeTable timeTable2 = TimeTable.of(timeTableReq2);
        timeTable2.connectMember(member);
        TimeTable timeTable3 = TimeTable.of(timeTableReq3);
        timeTable3.connectMember(member);
        TimeTable timeTable4 = TimeTable.of(timeTableReq4);
        timeTable4.connectMember(member);
        TimeTable timeTable5 = TimeTable.of(timeTableReq5);
        timeTable5.connectMember(member);
        TimeTable timeTable6 = TimeTable.of(timeTableReq6);
        timeTable6.connectMember(member);
        timeTableRepository.saveAllAndFlush(List.of(timeTable1, timeTable2, timeTable3, timeTable4, timeTable5, timeTable6));

        //when
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v2/timetable/list")
                                .param("startTime", registeredDateTime.toString())
                                .param("endTime", registeredDateTime.plusDays(1).toString())
                                .param("status", "OPERATE")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        String content = mvcResult.getResponse().getContentAsString();
        List<TimeTableRes> timeTableResList = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, TimeTableRes.class));
        assertThat(timeTableResList).hasSize(6)
                .extracting("title")
                .containsExactlyInAnyOrder("testTitle1", "testTitle2", "testTitle3", "testTitle4", "testTitle5", "testTitle6");
    }

    private static TimeTableReq createTimeTableReqCount(LocalDateTime registeredDateTime, int count) {
        return TimeTableReq.builder()
                .title("testTitle" + count)
                .startTime(registeredDateTime)
                .endTime(registeredDateTime.plusHours(1))
                .status("OPERATE")
                .build();
    }
}