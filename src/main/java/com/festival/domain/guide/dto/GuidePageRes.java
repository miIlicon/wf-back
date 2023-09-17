package com.festival.domain.guide.dto;

import com.festival.domain.guide.model.Guide;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GuidePageRes {

    private List<GuideRes> guideResList;
    private long totalCount;
    private int totalPage;
    private int pageNumber;
    private int pageSize;

    public static GuidePageRes of(Page<Guide> page){
        return GuidePageRes.builder()
                .guideResList(page.getContent().stream().map(GuideRes::of).collect(Collectors.toList()))
                .totalPage(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }
}
