package com.festival.old.domain.film.data.dto;

import com.festival.old.domain.film.data.entity.Film;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilmRes {
    private String title;

    private String subTitle;

    private String content;

    private String youtubeUrl;

    private String youtubeImgUrl;

    @Builder
    public FilmRes(String title, String subTitle, String content, String youtubeUrl, String youtubeImgUrl) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.youtubeUrl = youtubeUrl;
        this.youtubeImgUrl = youtubeImgUrl;
    }

    public static FilmRes of(Film film)
    {
        return FilmRes.builder()
                .title(film.getTitle())
                .subTitle(film.getSubTitle())
                .content(film.getContent())
                .youtubeUrl(film.getYoutubeUrl())
                .youtubeImgUrl(film.getYoutubeImgUrl())
                .build();
    }
}
