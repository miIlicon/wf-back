package com.festival.domain.bambooforest.dto;

import com.festival.domain.bambooforest.model.BamBooForest;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BamBooForestPageRes {

    private List<BamBooForestRes> forestResList;
    private long totalCount;
    private int totalPage;
    private int pageNumber;
    private int pageSize;

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
