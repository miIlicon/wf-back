package com.festival.domain.film.data.entity;


import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.film.data.dto.FilmReq;
import com.festival.domain.info.festivalEvent.data.entity.FestivalEventImage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Long id;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "subTitle",nullable = false)
    private String subTitle;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "youtubeUrl",nullable = false)
    private String youtubeUrl;

    @Column(name = "youtubeImgUrl",nullable = false) // 경도
    private String youtubeImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Admin admin;

    @Builder
    public Film(String title, String subTitle, String content, String youtubeUrl, String youtubeImgUrl, Admin admin) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.youtubeUrl = youtubeUrl;
        this.youtubeImgUrl = youtubeImgUrl;
        this.admin = admin;
    }

    public static Film of(FilmReq filmReq, Admin admin){
        return Film.builder()
                .title(filmReq.getTitle())
                .subTitle(filmReq.getSubTitle())
                .content(filmReq.getContent())
                .youtubeUrl(filmReq.getYoutubeUrl())
                .youtubeImgUrl(filmReq.getYoutubeUrl())
                .admin(admin)
                .build();
    }

    public void modify(FilmReq filmReq) {
        this.title = filmReq.getTitle();
        this.subTitle = filmReq.getSubTitle();
        this.content = filmReq.getContent();
        this.youtubeUrl = filmReq.getYoutubeUrl();
        this.youtubeImgUrl = filmReq.getYoutubeImgUrl();
    }
}
