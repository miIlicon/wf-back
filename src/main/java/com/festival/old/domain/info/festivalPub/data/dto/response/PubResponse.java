package com.festival.old.domain.info.festivalPub.data.dto.response;

import com.festival.old.domain.info.festivalPub.data.entity.pub.Pub;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PubResponse {

    private String title;
    private String subTitle;

    private String content;

    private String mainFilePath;
    private List<String> subFilePaths;

    private float latitude;
    private float longitude;

    private Boolean state;

    @Builder
    @QueryProjection
    public PubResponse(String title, String subTitle, String content,
                       String mainFilePath, List<String> subFilePaths, float latitude, float longitude, Boolean state) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.mainFilePath = mainFilePath;
        this.subFilePaths = subFilePaths;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
    }

    public static PubResponse of(final Pub pub, String filePath) {

        List<String> list = new ArrayList<>();
        for (String subFilePath : pub.getPubImage().getSubFileNames()) {
            list.add(filePath + subFilePath);
        }

        return PubResponse.builder()
                .title(pub.getTitle())
                .subTitle(pub.getSubTitle())
                .content(pub.getContent())
                .mainFilePath(filePath + pub.getPubImage().getMainFileName())
                .subFilePaths(list)
                .latitude(pub.getLatitude())
                .longitude(pub.getLongitude())
                .state(pub.getPubState())
                .build();
    }
}
