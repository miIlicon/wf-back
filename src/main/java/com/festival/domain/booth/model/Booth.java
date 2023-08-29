package com.festival.domain.booth.model;

import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.image.model.Image;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Booth{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subTitle;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private float latitude;

    @Column(nullable = false) // 경도
    private float longitude;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoothStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoothType type;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Image image;

    private String mainFilePath;

    @ElementCollection
    private List<String> subFilePaths;



    @Builder
    private Booth(String title, String subTitle, String content, float latitude, float longitude,BoothStatus status, BoothType type) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.type = type;
    }


    public static Booth of(BoothReq boothReq){
        return Booth.builder()
                .title(boothReq.getTitle())
                .subTitle(boothReq.getSubTitle())
                .content(boothReq.getContent())
                .latitude(boothReq.getLatitude())
                .longitude(boothReq.getLongitude())
                .status(BoothStatus.handleStatus(boothReq.getStatus()))
                .type(BoothType.handleType(boothReq.getType())).build();
    }

    public void update(BoothReq boothReq){
        this.title = boothReq.getTitle();
        this.subTitle = boothReq.getSubTitle();
        this.content =  boothReq.getContent();
        this.latitude = boothReq.getLatitude();
        this.longitude = boothReq.getLongitude();
        this.status = BoothStatus.handleStatus(boothReq.getStatus());
        this.type = BoothType.handleType(boothReq.getType());
    }
    public void delete()
    {
        this.status = BoothStatus.TERMINATE;
    }
}
