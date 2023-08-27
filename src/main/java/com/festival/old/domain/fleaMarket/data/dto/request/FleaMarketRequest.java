package com.festival.old.domain.fleaMarket.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FleaMarketRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "소제목을 입력해주세요.")
    private String subTitle;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "위치를 입력해주세요.")
    private float latitude;

    @NotNull(message = "위치를 입력해주세요.")
    private float longitude;

    @NotNull(message = "상태를 입력해주세요.")
    private Boolean state;

    public FleaMarketRequest(String title, String subTitle, String content, float latitude, float longitude, Boolean state) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
    }
}
