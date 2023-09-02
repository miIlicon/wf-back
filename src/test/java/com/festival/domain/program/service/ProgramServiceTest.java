package com.festival.domain.program.service;


import com.festival.common.base.OperateStatus;
import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.image.fixture.ImageFixture;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.fixture.MemberFixture;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.repository.MemberRepository;
import com.festival.domain.member.service.MemberService;
import com.festival.domain.program.dto.ProgramListReq;
import com.festival.domain.program.dto.ProgramReq;
import com.festival.domain.program.dto.ProgramRes;
import com.festival.domain.program.fixture.ProgramFixture;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.repository.ProgramRepository;
import com.festival.domain.program.service.vo.ProgramSearchCond;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
        ProgramReq programReq = ProgramReq.builder()
                .title("프로그램 게시물 제목")
                .subTitle("프로그램 게시물 부제목")
                .status("OPERATE")
                .content("프로그램 게시물 내용")
                .longitude(50)
                .latitude(50)
                .mainFile(generateMockImageFile("mainFile"))
                .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                .type("EVENT")
                .build();

        Program program = ProgramFixture.EVENT;
        ReflectionTestUtils.setField(program, "id",1L);

        given(programRepository.save(any(Program.class)))
                .willReturn(program);

        //when
        Long programId = programService.createProgram(programReq);

        //then
        Assertions.assertThat(programId).isEqualTo(1L);
    }

        @DisplayName("프로그램 업데이트하면 programId를 반환한다.")
        @Test
        void updateBooth() throws IOException {

            ProgramReq programReq = ProgramReq.builder()
                    .title("프로그램 게시물 제목")
                    .subTitle("프로그램 게시물 부제목")
                    .status("OPERATE")
                    .content("프로그램 게시물 내용")
                    .longitude(50)
                    .latitude(50)
                    .mainFile(generateMockImageFile("mainFile"))
                    .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                    .type("EVENT")
                    .build();

            Program program = ProgramFixture.EVENT;
            Member member1 = MemberFixture.member1;

            program.connectMember(member1);
            ReflectionTestUtils.setField(program, "id",1L);

            given(programRepository.findById(1L))
                    .willReturn(Optional.of(program));
            given(memberService.getAuthenticationMember())
                    .willReturn(member1);


            //when
            Long programId = programService.updateProgram(1L, programReq);

            //then
            Assertions.assertThat(programId).isEqualTo(1L);
        }


        @DisplayName("존재하지 않는 프로그램을 업데이트하면 NotFoundException을 반환한다.")
        @Test
        void updateNonExistsProgram() throws IOException {
    
            //given
            ProgramReq programUpdateReq = ProgramReq.builder()
                    .title("프로그램 게시물 제목")
                    .subTitle("프로그램 게시물 부제목")
                    .status("OPERATE")
                    .content("프로그램 게시물 내용")
                    .longitude(50)
                    .latitude(50)
                    .mainFile(generateMockImageFile("mainFile"))
                    .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                    .type("EVENT")
                    .build();

    
            given(programRepository.findById(1L))
                    .willReturn(Optional.empty());
    
            //when & then
            assertThatThrownBy(() -> programService.updateProgram(1L, programUpdateReq))
                    .isInstanceOf(NotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.NOT_FOUND_PROGRAM);
        }

    @DisplayName("삭제된 프로그램을 업데이트하면 AleadyDeletedException을 반환한다.")
    @Test
    void updateDeletedProgram() throws IOException {

        //given
        ProgramReq programUpdateReq = ProgramReq.builder()
                .title("프로그램 게시물 제목")
                .subTitle("프로그램 게시물 부제목")
                .status("OPERATE")
                .content("프로그램 게시물 내용")
                .longitude(50)
                .latitude(50)
                .mainFile(generateMockImageFile("mainFile"))
                .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                .type("EVENT")
                .build();

        Program program = ProgramFixture.EVENT;
        ReflectionTestUtils.setField(program, "status", OperateStatus.TERMINATE);

        given(programRepository.findById(1L))
                .willReturn(Optional.of(program));

        //when & then
        assertThatThrownBy(() -> programService.updateProgram(1L, programUpdateReq))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);
    }
    @DisplayName("자신의 게시물이 아닌 프로그램을 업데이트하면 ForbiddenException을 반환한다.")
    @Test
    void updateNotMineProgram() throws IOException {

        //given
        ProgramReq programUpdateReq = ProgramReq.builder()
                .title("프로그램 게시물 제목")
                .subTitle("프로그램 게시물 부제목")
                .status("OPERATE")
                .content("프로그램 게시물 내용")
                .longitude(50)
                .latitude(50)
                .mainFile(generateMockImageFile("mainFile"))
                .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                .type("EVENT")
                .build();
        Program program = ProgramFixture.EVENT;
        program.connectMember(MemberFixture.member1);
        Member member2 = MemberFixture.member2;


        given(programRepository.findById(1L))
                .willReturn(Optional.of(program));
        given(memberService.getAuthenticationMember())
                .willReturn(member2);

        //when & then
        assertThatThrownBy(() -> programService.updateProgram(1L, programUpdateReq))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_UPDATE);
    }


    @DisplayName("아이디에 맞는 프로그램을 반환한다.")
    @Test
    void getProgram(){
        //given
        Program event = ProgramFixture.EVENT;
        ReflectionTestUtils.setField(event, "image", ImageFixture.image);

        given(programRepository.findById(1L))
                .willReturn(Optional.of(event));
        //when
        ProgramRes programRes = programService.getProgram(1L);

        //then
        Assertions.assertThat(programRes).usingRecursiveComparison()
                .isEqualTo(ProgramRes.of(event));
    }

    @DisplayName("조건에 맞는 프로그램 리스트 반환한다.")
    @Test
    void getBoothList(){
        //given
        Program event = ProgramFixture.EVENT;
        ReflectionTestUtils.setField(event, "image", ImageFixture.image);
        Program game = ProgramFixture.GAME;
        ReflectionTestUtils.setField(game, "image", ImageFixture.image);


        given(programRepository.getList(any(ProgramSearchCond.class), any(Pageable.class)))
                .willReturn(List.of(event, game));
        //when

        ProgramListReq programListReq = ProgramListReq.builder()
                .status("OPERATE")
                .type("EVENT")
                .build();


        List<ProgramRes> programList = programService.getProgramList(programListReq, PageRequest.of(0, 3));

        //then
        Assertions.assertThat(programList).hasSize(2);
    }

}