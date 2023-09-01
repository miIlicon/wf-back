package com.festival.domain.program.service;


import com.festival.domain.booth.controller.dto.BoothListReq;
import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.fixture.BoothFixture;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.repository.BoothRepository;
import com.festival.domain.booth.service.BoothService;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.festival.domain.image.fixture.ImageFixture;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.fixture.MemberFixture;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.repository.MemberRepository;
import com.festival.domain.member.service.MemberService;
import com.festival.domain.program.dto.ProgramReq;
import com.festival.domain.program.fixture.ProgramFixture;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.repository.ProgramRepository;
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

import static com.festival.domain.member.fixture.MemberFixture.member;
import static com.festival.domain.util.TestImageUtils.generateMockImageFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;

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
            Member member = MemberFixture.member;

            program.connectMember(member);
            ReflectionTestUtils.setField(program, "id",1L);

            given(programRepository.findById(1L))
                    .willReturn(Optional.of(program));
            given(memberService.getAuthenticationMember())
                    .willReturn(member);


            //when
            Long programId = programService.updateProgram(1L, programReq);

            //then
            Assertions.assertThat(programId).isEqualTo(1L);
        }

        /*
        @DisplayName("존재하지 않는 부스를 업데이트하면 NotFoundException을 반환한다.")
        @Test
        void updateNonExistsBooth() throws IOException {
    
            //given
            BoothReq boothReq = BoothReq.builder()
                    .title("변경된 제목")
                    .subTitle("변경된 부제목")
                    .status("OPERATE")
                    .content("변경된 내용")
                    .longitude(50)
                    .latitude(50)
                    .mainFile(generateMockImageFile("mainFile"))
                    .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                    .type("FOOD_TRUCK")
                    .build();
    
    
            given(boothRepository.findById(1L))
                    .willThrow(new NotFoundException(NOT_FOUND_BOOTH));
    
            //when & then
            assertThatThrownBy(() -> boothService.updateBooth(boothReq,1L, "username"))
                    .isInstanceOf(NotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(NOT_FOUND_BOOTH);
        }
    
        @DisplayName("존재하지 않는 부스를 삭제하면 NotFoundException를 반환한다.")
        @Test
        void updateBooth() throws IOException {
    
            //given
            given(boothRepository.findById(1L))
                    .willThrow(new NotFoundException(NOT_FOUND_BOOTH));
    
    
            //when & then
            assertThatThrownBy(() -> boothService.deleteBooth(1L, "username"))
                    .isInstanceOf(NotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(NOT_FOUND_BOOTH);
        }

    @DisplayName("아이디에 맞는 부스를 반환한다.")
    @Test
    void getBooth(){
        //given
        Booth foodTruck = BoothFixture.FOOD_TRUCK;
        ReflectionTestUtils.setField(foodTruck, "image", ImageFixture.image);

        given(programRepository.findById(1L))
                .willReturn(Optional.of(foodTruck));
        //when
        BoothRes boothRes = boothService.getBooth(1L);

        //then
        Assertions.assertThat(boothRes).usingRecursiveComparison()
                .isEqualTo(BoothRes.of(foodTruck));
    }

    @DisplayName("조건에 맞는 부스 리스트 반환한다.")
    @Test
    void getBoothList(){
        //given
        Booth pub = BoothFixture.PUB;
        ReflectionTestUtils.setField(pub, "image", ImageFixture.image);

        given(boothRepository.getList(any(BoothListSearchCond.class), any(Pageable.class)))
                .willReturn(List.of(pub));
        //when

        BoothListReq boothListReq = BoothListReq.builder()
                .type("PUB")
                .status("OPERATE")
                .build();
        Pageable pageable =  PageRequest.of(0, 3);

        List<BoothRes> boothRes = boothService.getBoothList(boothListReq, pageable);

        //then
        Assertions.assertThat(boothRes).hasSize(1);
    }
        */
}