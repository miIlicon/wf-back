package com.festival.domain.program.model;


import com.festival.common.base.BaseEntity;
import com.festival.domain.image.model.Image;
import com.festival.domain.member.model.Member;
import com.festival.domain.program.dto.ProgramReq;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Program extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title", nullable = false)
    private String subTitle;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "latitude", nullable = false) // 위도
    private float latitude;

    @Column(name = "longitude", nullable = false) // 경도
    private float longitude;

    @Column(name = "status", nullable = false)
    private ProgramStatus status;

    @Column(name = "type", nullable = false)
    private ProgramType type;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Member member;

    @Builder
    private Program(Long id, String title, String subTitle, String content, float latitude, float longitude, ProgramStatus status, ProgramType type) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.type = type;
    }

    public static Program of(ProgramReq programReq) {
        return Program.builder()
                .title(programReq.getTitle())
                .subTitle(programReq.getSubTitle())
                .content(programReq.getContent())
                .latitude(programReq.getLatitude())
                .longitude(programReq.getLongitude())
                .status(ProgramStatus.handleStatus(programReq.getStatus()))
                .type(ProgramType.handleType(programReq.getType()))
                .build();
    }

    public void update(ProgramReq programReqDto) {
        this.title = programReqDto.getTitle();
        this.subTitle = programReqDto.getSubTitle();
        this.content = programReqDto.getContent();
        this.latitude = programReqDto.getLatitude();
        this.longitude = programReqDto.getLongitude();
        this.status = ProgramStatus.handleStatus(programReqDto.getStatus());
        this.type = ProgramType.handleType(programReqDto.getType());
    }

    public void setStatus(ProgramStatus newStatus) {
        this.status = newStatus;
    }

    public void setImage(Image uploadImage) {
        this.image = uploadImage;
    }
}
