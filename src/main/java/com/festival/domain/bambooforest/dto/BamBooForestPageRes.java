package com.festival.domain.bambooforest.dto;

import com.festival.domain.bambooforest.model.BamBooForest;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BamBooForestPageRes {

    private List<BamBooForestRes> forestResList;
    private long totalCount;
    private int totalPage;
    private int pageNumber;
    private int pageSize;

    @Builder
    public BamBooForestPageRes(List<BamBooForestRes> forestResList, long totalCount, int totalPage, int pageNumber, int pageSize) {
        this.forestResList = forestResList;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static BamBooForestPageRes of(Page<BamBooForest> page){
        return BamBooForestPageRes.builder()
                .forestResList(page.getContent().stream().map(BamBooForestRes::of).collect(Collectors.toList()))
                .totalPage(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }
}
