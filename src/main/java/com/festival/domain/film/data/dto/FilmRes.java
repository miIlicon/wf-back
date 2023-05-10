package com.festival.domain.film.data.dto;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.film.data.entity.Film;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilmRes {
    private Long id;

    private String title;

    private String subTitle;

    private String content;

    private String youtubeUrl;

    private String youtubeImgUrl;

    private Long adminId;

    @Builder
    public FilmRes(Long id, String title, String subTitle, String content, String youtubeUrl, String youtubeImgUrl,Long adminId) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.youtubeUrl = youtubeUrl;
        this.youtubeImgUrl = youtubeImgUrl;
        this.adminId = adminId;
    }

    public static FilmRes of(Film film)
    {
        return FilmRes.builder()
                .id(film.getId())
                .title(film.getTitle())
                .subTitle(film.getSubTitle())
                .content(film.getContent())
                .youtubeUrl(film.getYoutubeUrl())
                .youtubeImgUrl(film.getYoutubeImgUrl())
                .adminId(film.getAdmin().getId())
                .build();
    }
}
