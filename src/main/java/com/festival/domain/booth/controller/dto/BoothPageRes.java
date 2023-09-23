package com.festival.domain.booth.controller.dto;

import com.festival.domain.booth.model.Booth;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BoothPageRes {

    private List<BoothRes> boothResList;
    private long totalCount;
    private int totalPage;
    private int pageNumber;
    private int pageSize;

    @Builder
    private BoothPageRes(List<BoothRes> boothResList, long totalCount, int totalPage, int pageNumber, int pageSize) {
        this.boothResList = boothResList;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static BoothPageRes of(Page<Booth> page){
        return BoothPageRes.builder()
                .boothResList(page.getContent().stream().map(BoothRes::of).collect(Collectors.toList()))
                .totalPage(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }

}
