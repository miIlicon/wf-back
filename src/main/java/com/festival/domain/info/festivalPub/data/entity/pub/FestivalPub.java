package com.festival.domain.info.festivalPub.data.entity.pub;

import com.festival.domain.info.festivalPub.data.entity.file.PubImage;
import com.festival.global.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalPub extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title", nullable = false)
    private String subTitle;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "pub_file_id")
    private PubImage pubImage;

    @Column(name = "latitude", nullable = false)
    private int latitude;

    @Column(name = "longitude", nullable = false)
    private int longitude;

    @Column(name = "pub_state", nullable = false)
    private PubState pubState;

    public FestivalPub(String title, String subTitle, String content, PubImage pubImage, int latitude, int longitude, PubState pubState) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.pubImage = pubImage;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pubState = pubState;
    }
}