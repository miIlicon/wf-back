package com.festival.domain.program.repository;

import com.festival.domain.image.model.Image;
import com.festival.domain.program.dto.ProgramPageRes;
import com.festival.domain.program.dto.ProgramSearchRes;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.service.vo.ProgramSearchCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static com.festival.common.base.OperateStatus.OPERATE;
import static com.festival.common.base.OperateStatus.UPCOMING;
import static com.festival.domain.program.model.ProgramType.EVENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("test")
@DataJpaTest
class ProgramRepositoryTest {

    @Autowired
    private ProgramRepository programRepository;

    @DisplayName("작일과 같으면서 운영중인 프로그램을 조회한다.")
    @Test
    void findByStartDateEqualsAndOperateStatusEquals() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2023, 11, 14);
        LocalDate endDate = LocalDate.of(2023, 11, 15);

        Program program1 = createdProgram(startDate, endDate, "title1");
        Program program2 = createdProgram(startDate, endDate, "title2");
        Program program3 = createdProgram(startDate, endDate, "title3");
        programRepository.saveAllAndFlush(List.of(program1, program2, program3));

        //when
        List<Program> programs = programRepository.findByStartDateEqualsAndOperateStatusEquals(startDate, OPERATE);

        //then
        assertThat(programs).hasSize(3)
                .extracting("title", "subTitle")
                .containsExactlyInAnyOrder(
                        tuple("title1", "title1"),
                        tuple("title2", "title2"),
                        tuple("title3", "title3")
                );
    }

    @DisplayName("종료일이 같은 프로그램을 조회한다.")
    @Test
    void findByEndDateEquals() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2023, 11, 14);
        LocalDate endDate = LocalDate.of(2023, 11, 15);

        Program program1 = createdProgram(startDate, endDate, "title1");
        Program program2 = createdProgram(startDate, endDate, "title2");
        Program program3 = createdProgram(startDate, endDate, "title3");
        programRepository.saveAllAndFlush(List.of(program1, program2, program3));

        //when
        List<Program> programs = programRepository.findByEndDateEquals(endDate);

        //then
        assertThat(programs).hasSize(3)
                .extracting("title", "subTitle")
                .containsExactlyInAnyOrder(
                        tuple("title1", "title1"),
                        tuple("title2", "title2"),
                        tuple("title3", "title3")
                );
    }

    @DisplayName("프로그램 리스트를 조회한다.")
    @Test
    void getProgramList() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2023, 11, 14);
        LocalDate endDate = LocalDate.of(2023, 11, 15);

        Program program1 = createdProgram(startDate, endDate, "title1");
        Program program2 = createdProgram(startDate, endDate, "title2");
        Program program3 = createdProgram(startDate, endDate, "title3");
        programRepository.saveAllAndFlush(List.of(program1, program2, program3));

        //when
        ProgramSearchCond programSearchCond = ProgramSearchCond.builder()
                .type(EVENT.getValue())
                .pageable(PageRequest.of(0, 6))
                .build();
        ProgramPageRes programPageRes = programRepository.getProgramList(programSearchCond);

        //then
        assertThat(programPageRes.getProgramList()).hasSize(3)
                .extracting("title", "subTitle")
                .containsExactlyInAnyOrder(
                        tuple("title1", "title1"),
                        tuple("title2", "title2"),
                        tuple("title3", "title3")
                );
        assertThat(programPageRes.getPageNumber()).isEqualTo(0);
        assertThat(programPageRes.getPageSize()).isEqualTo(6);
        assertThat(programPageRes.getTotalCount()).isEqualTo(3);
        assertThat(programPageRes.getTotalPage()).isEqualTo(1);
    }

    @DisplayName("키워드가 포함된 이름의 프로그램 리스트를 조회한다. 이때 운영중인 프로그램만 조회한다.")
    @Test
    void searchProgramList() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2023, 11, 14);
        LocalDate endDate = LocalDate.of(2023, 11, 15);

        Program program1 = createdProgram(startDate, endDate, "title1");
        Program program2 = createdProgram(startDate, endDate, "title2");
        Program program3 = createdProgram(startDate, endDate, "title3");
        programRepository.saveAllAndFlush(List.of(program1, program2, program3));

        //when
        String keyword = "title2";
        List<ProgramSearchRes> programSearchRes = programRepository.searchProgramList(keyword);

        //then
        assertThat(programSearchRes).hasSize(1)
                .extracting("title", "subTitle", "operateStatus", "mainFilePath")
                .containsExactlyInAnyOrder(
                        tuple("title2", "title2", "OPERATE", "mainFilePath")
                );
    }

    private Program createdProgram(LocalDate startDate, LocalDate endDate, String title) {
        Program program = Program.builder()
                .title(title)
                .subTitle(title)
                .content("content")
                .latitude(1.0f)
                .longitude(1.0f)
                .type(EVENT)
                .operateStatus(OPERATE)
                .startDate(startDate)
                .endDate(endDate)
                .deleted(false)
                .build();
        program.setImage(Image.builder()
                .mainFilePath("mainFilePath")
                .subFilePaths(List.of("subFilePaths"))
                .build());
        return program;
    }

}