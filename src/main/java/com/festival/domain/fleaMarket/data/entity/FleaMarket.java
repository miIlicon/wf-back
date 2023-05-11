package com.festival.domain.fleaMarket.data.entity;

import com.festival.common.base.BaseTimeEntity;
import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.info.festivalPub.data.dto.request.PubRequest;
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
    @JoinColumn(name = "market_image_id")
    private FleaMarketImage marketImage;

    @Column(name = "latitude", nullable = false) // 위도
    private int latitude;

    @Column(name = "longitude", nullable = false) // 경도
    private int longitude;

    @Column(name = "pub_state", nullable = false)
    private Boolean marketState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    public FleaMarket(String title, String subTitle, String content, int latitude, int longitude, Boolean marketState, Admin admin) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.marketState = marketState;
        this.admin = admin;
    }

    public FleaMarket(PubRequest pubRequest, Admin admin) {
        this.title = pubRequest.getTitle();
        this.subTitle = pubRequest.getSubTitle();
        this.content = pubRequest.getContent();
        this.latitude = pubRequest.getLatitude();
        this.longitude = pubRequest.getLongitude();
        this.marketState = pubRequest.getPubState();
        this.admin = admin;
    }

    public void connectMarketImage(FleaMarketImage marketImage) {
        this.marketImage = marketImage;
    }
}