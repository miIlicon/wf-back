package com.festival.domain.film.data.dto;

import com.festival.domain.film.data.entity.Film;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilmListRes {
    private Long id;

    private String title;

    private String subTitle;

    private String content;

    private String youtubeUrl;

    private String youtubeImgUrl;

    @Builder
    public FilmListRes(Long id, String title, String subTitle, String content, String youtubeUrl, String youtubeImgUrl) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.youtubeUrl = youtubeUrl;
        this.youtubeImgUrl = youtubeImgUrl;
    }

    public static FilmListRes of(Film film)
    {
        return FilmListRes.builder()
                .title(film.getTitle())
                .subTitle(film.getSubTitle())
                .content(film.getContent())
                .youtubeUrl(film.getYoutubeUrl())
                .youtubeImgUrl(film.getYoutubeImgUrl())
                .build();
    }
}