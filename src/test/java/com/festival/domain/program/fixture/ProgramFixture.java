package com.festival.domain.program.fixture;

import com.festival.common.base.OperateStatus;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.model.ProgramType;

public class ProgramFixture {
    public static Program EVENT = Program.builder()
            .title("이벤트 게시물 제목")
            .subTitle("이벤트 게시물 부제목")
            .operateStatus(OperateStatus.OPERATE)
            .content("이벤트 게시물 내용")
            .longitude(50)
            .latitude(50)
            .type(ProgramType.EVENT)
            .build();


    public static Program GAME = Program.builder()
            .title("게임 게시물 제목")
            .subTitle("게임 게시물 부제목")
            .operateStatus(OperateStatus.OPERATE)
            .content("게임 게시물 내용")
            .longitude(50)
            .latitude(50)
            .type(ProgramType.GAME)
            .build();

    public static Program DELETED_PROGRAM = Program.builder()
            .title("삭제된 프로그램 게시물 제목")
            .subTitle("삭제된 프로그램 게시물 부제목")
            .operateStatus(OperateStatus.TERMINATE)
            .content("삭제된 프로그램 게시물 내용")
            .longitude(50)
            .latitude(50)
            .type(ProgramType.GAME)
            .deleted(true)
            .build();
}
