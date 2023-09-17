package com.festival.domain.program.dto;

import com.festival.domain.program.model.Program;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProgramPageRes {

    private List<ProgramRes> programList;
    private long totalCount;
    private int totalPage;
    private int pageNumber;
    private int pageSize;

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
