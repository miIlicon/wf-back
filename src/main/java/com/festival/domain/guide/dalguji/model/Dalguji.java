package com.festival.domain.guide.dalguji.model;

import com.festival.common.base.BaseEntity;
import com.festival.domain.guide.dalguji.dto.DalgujiReq;
import com.festival.domain.image.model.Image;
import com.festival.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Dalguji extends BaseEntity {

    @Id
    @Column(name = "college", nullable = false)
    private String college;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    public Dalguji(String college, List<Image> images) {
        this.images = images;
    }

    public void addImages(List<Image> images) {
        this.images.addAll(images);
    }

    public void deleteImage(Image image) {
        this.images.remove(image);
    }

    public void deleteImageAll() {
        this.images = null;
    }

    public void connectMember(Member member) {
        this.member = member;
    }
}
