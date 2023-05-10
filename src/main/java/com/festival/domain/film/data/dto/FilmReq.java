package com.festival.domain.film.data.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilmReq {

    @NotNull(message = "상태를 입력해주세요.")
    private String title;

    @NotNull(message = "상태를 입력해주세요.")
    private String subTitle;

    @NotNull(message = "상태를 입력해주세요.")
    private String content;

    @NotNull(message = "상태를 입력해주세요.")
    private String youtubeUrl;

    @NotNull(message = "상태를 입력해주세요.")
    private String youtubeImg;
}
