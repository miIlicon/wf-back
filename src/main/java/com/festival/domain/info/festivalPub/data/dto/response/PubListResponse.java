package com.festival.domain.info.festivalPub.data.dto.response;

import com.festival.domain.info.festivalPub.data.entity.pub.Pub;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PubListResponse {

    private Long id;
    private String title;
    private String subTitle;
    private String mainFilePath;
    private Boolean pubState;

    @Builder
    @QueryProjection
    public PubListResponse(Long id, String title, String subTitle,
                           String mainFilePath, Boolean pubState) {
        this.id= id;
        this.title = title;
        this.subTitle = subTitle;
        this.mainFilePath = mainFilePath;
        this.pubState = pubState;
    }

    public static PubListResponse of(final Pub pub, String filePath) {

        List<String> list = new ArrayList<>();
        for (String subFilePath : pub.getPubImage().getSubFileNames()) {
            list.add(filePath + subFilePath);
        }

        return PubListResponse.builder()
                .id(pub.getId())
                .title(pub.getTitle())
                .subTitle(pub.getSubTitle())
                .mainFilePath(filePath + pub.getPubImage().getMainFileName())
                .pubState(pub.getPubState())
                .build();
    }
}