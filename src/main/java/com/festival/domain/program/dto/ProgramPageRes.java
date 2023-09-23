package com.festival.domain.program.dto;

import com.festival.domain.program.model.Program;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ProgramPageRes {

    private List<ProgramRes> programList;
    private long totalCount;
    private int totalPage;
    private int pageNumber;
    private int pageSize;

    @Builder
    private ProgramPageRes(List<ProgramRes> programList, long totalCount, int totalPage, int pageNumber, int pageSize) {
        this.programList = programList;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static ProgramPageRes of(Page<Program> page){
        return ProgramPageRes.builder()
                .programList(page.getContent().stream().map(ProgramRes::of).collect(Collectors.toList()))
                .totalPage(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }
}
