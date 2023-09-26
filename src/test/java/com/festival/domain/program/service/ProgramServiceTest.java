package com.festival.domain.program.service;

import com.festival.common.base.OperateStatus;
import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;

import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.fixture.MemberFixture;
import com.festival.domain.member.repository.MemberRepository;
import com.festival.domain.member.service.MemberService;
import com.festival.domain.program.dto.ProgramReq;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.model.ProgramType;
import com.festival.domain.program.repository.ProgramRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.festival.domain.program.fixture.ProgramFixture.DELETED_PROGRAM;
import static com.festival.domain.util.TestImageUtils.generateMockImageFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProgramServiceTest {

    @InjectMocks
    private ProgramService programService;

    @Mock
    private MemberService memberService;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private ImageService imageService;
    @Mock
    private MemberRepository memberRepository;


    @DisplayName("축제 프로그램을 생성한 후 programId를 반환한다.")
    @Test
    void createProgram() throws IOException {

        //given
        ProgramReq programReq = getProgramCreateReq();

        given(programRepository.save(any(Program.class)))
                .willReturn(getProgram());

        //when
        Long programId = programService.createProgram(programReq, LocalDate.now());

        //then
        Assertions.assertThat(programId).isEqualTo(1L);
    }

    @DisplayName("존재하지 않는 부스를 업데이트하면 NotFoundException을 반환한다.")
    @Test
    void updateNotExistProgram() throws IOException {
        //given
        ProgramReq programUpdateReq = getProgramUpdateReq();


        given(programRepository.findById(1L))
                .willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> programService.updateProgram(1L, programUpdateReq))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_PROGRAM);

    }
    @DisplayName("삭제된 부스를 업데이트하면 AlreadyDeletedException을 반환한다.")
    @Test
    void updateDeletedProgram() throws IOException {
        //given
        ProgramReq programUpdateReq = getProgramUpdateReq();

        given(programRepository.findById(1L))
                .willReturn(Optional.of(DELETED_PROGRAM));

        //when & then
        assertThatThrownBy(() -> programService.updateProgram(1L, programUpdateReq))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);
    }

    @DisplayName("다른 사람이 부스를 업데이트하면 ForbiddenException을 반환한다.")
    @Test
    void updateProgram2() throws IOException {
        //given
        ProgramReq programUpdateReq = getProgramUpdateReq();
        Program program = getProgram();
        program.connectMember(MemberFixture.MANAGER1);

        given(programRepository.findById(1L))
                .willReturn(Optional.of(program));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER2);

        //when & then
        assertThatThrownBy(() -> programService.updateProgram(1L, programUpdateReq))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_UPDATE);
    }
    @DisplayName("Admin이 부스를 업데이트하면 programId를 반환한다.")
    @Test
    void updateProgram3() throws IOException {
        //given
        ProgramReq programUpdateReq = getProgramUpdateReq();
        Program program = getProgram();
        program.connectMember(MemberFixture.MANAGER1);

        given(programRepository.findById(1L))
                .willReturn(Optional.of(program));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when
        Long programId = programService.updateProgram(1L, programUpdateReq);

        //then
        Assertions.assertThat(programId).isEqualTo(program.getId());
    }
    @DisplayName("부스의 관리자가 업데이트하면 programId를 반환한다.")
    @Test
    void updateProgram4() throws IOException {
        //given
        ProgramReq programUpdateReq = getProgramUpdateReq();
        Program program = getProgram();
        program.connectMember(MemberFixture.MANAGER1);

        given(programRepository.findById(1L))
                .willReturn(Optional.of(program));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER1);

        //when
        Long programId = programService.updateProgram(1L, programUpdateReq);

        //then
        Assertions.assertThat(programId).isEqualTo(program.getId());
    }

    @DisplayName("존재하지 않는 부스를 삭제하면 NotFoundException을 반환한다.")
    @Test
    void deleteNotExistProgram() {
        //given
        given(programRepository.findById(1L))
                .willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> programService.deleteProgram(1L))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_PROGRAM);

    }
    @DisplayName("삭제된 부스를 삭제하면 AlreadyDeletedException을 반환한다.")
    @Test
    void deleteDeletedProgram() {
        //given
        given(programRepository.findById(1L))
                .willReturn(Optional.of(DELETED_PROGRAM));

        //when & then
        assertThatThrownBy(() -> programService.deleteProgram(1L))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);

    }

    @DisplayName("다른 사람이 부스를 삭제하면 ForbiddenException을 반환한다.")
    @Test
    void deleteProgram2() throws IOException {
        //given
        Program program = getProgram();
        program.connectMember(MemberFixture.MANAGER1);

        given(programRepository.findById(1L))
                .willReturn(Optional.of(program));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER2);

        //when & then
        assertThatThrownBy(() -> programService.deleteProgram(1L))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_DELETE);
    }
    @DisplayName("Admin이 부스를 삭제하면 정상처리")
    @Test
    void deleteprogram3() {
        //given
        Program program = getProgram();
        program.connectMember(MemberFixture.MANAGER1);

        given(programRepository.findById(1L))
                .willReturn(Optional.of(program));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when && then
        programService.deleteProgram(1L);
    }
    @DisplayName("부스의 관리자가 업데이트하면 정상동작")
    @Test
    void deleteprogram4(){
        //given
        Program program = getProgram();
        program.connectMember(MemberFixture.MANAGER1);

        given(programRepository.findById(1L))
                .willReturn(Optional.of(program));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER1);

        //when && then
        programService.deleteProgram(1L);
    }
    private Program getProgram(){
        Program program = Program.builder()
                .title("프로그램 게시물 제목")
                .subTitle("프로그램 게시물 부제목")
                .operateStatus(OperateStatus.OPERATE)
                .content("프로그램 게시물 내용")
                .longitude(50.0f)
                .latitude(50.0f)
                .type(ProgramType.EVENT)
                .build();
        ReflectionTestUtils.setField(program, "id", 1L);
        return program;
    }
    private ProgramReq getProgramCreateReq() throws IOException {
        ProgramReq programReq = ProgramReq.builder()
                .title("프로그램 게시물 제목")
                .subTitle("프로그램 게시물 부제목")
                .operateStatus("OPERATE")
                .content("프로그램 게시물 내용")
                .longitude(50.0f)
                .latitude(50.0f)
                .mainFile(generateMockImageFile("mainFile"))
                .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                .type("EVENT")
                .build();
        return programReq;
    }
    private ProgramReq getProgramUpdateReq() throws IOException {
        ProgramReq programReq = ProgramReq.builder()
                .title("프로그램 게시물 제목 수정")
                .subTitle("프로그램 게시물 부제목 수정")
                .operateStatus("TERMINATE")
                .content("프로그램 게시물 내용 수정")
                .longitude(50.0f)
                .latitude(50.0f)
                .mainFile(generateMockImageFile("mainFile"))
                .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                .type("EVENT")
                .build();
        return programReq;
    }
}