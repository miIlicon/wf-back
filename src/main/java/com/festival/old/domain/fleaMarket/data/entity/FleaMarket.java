package com.festival.old.domain.fleaMarket.data.entity;

import com.festival.old.common.base.BaseTimeEntity;
import com.festival.old.domain.admin.data.entity.Admin;
import com.festival.old.domain.fleaMarket.data.dto.request.FleaMarketRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FleaMarket extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title", nullable = false)
    private String subTitle;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "flea_market_image_id")
    private FleaMarketImage fleaMarketImage;

    @Column(name = "latitude", nullable = false) // 위도
    private float latitude;

    @Column(name = "longitude", nullable = false) // 경도
    private float longitude;

    @Column(name = "flea_market_state", nullable = false)
    private Boolean fleaMarketState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Admin admin;

    public FleaMarket(String title, String subTitle, String content, float latitude, float longitude, Boolean fleaMarketState) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fleaMarketState = fleaMarketState;
    }

    public FleaMarket(FleaMarketRequest fleaMarketRequest) {
        this.title = fleaMarketRequest.getTitle();
        this.subTitle = fleaMarketRequest.getSubTitle();
        this.content = fleaMarketRequest.getContent();
        this.latitude = fleaMarketRequest.getLatitude();
        this.longitude = fleaMarketRequest.getLongitude();
        this.fleaMarketState = fleaMarketRequest.getState();
    }

    public void connectAdmin(Admin admin) {
        this.admin = admin;
    }

    public void connectMarketImage(FleaMarketImage fleaMarketImage) {
        this.fleaMarketImage = fleaMarketImage;
    }

    public void modify(FleaMarketRequest fleaMarketRequest) {
        this.title = fleaMarketRequest.getTitle();
        this.subTitle = fleaMarketRequest.getSubTitle();
        this.content = fleaMarketRequest.getContent();
        this.latitude = fleaMarketRequest.getLatitude();
        this.longitude = fleaMarketRequest.getLongitude();
        this.fleaMarketState = fleaMarketRequest.getState();
    }
}