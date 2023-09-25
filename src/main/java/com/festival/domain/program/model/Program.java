package com.festival.domain.program.model;


import com.festival.common.base.BaseEntity;
import com.festival.common.base.OperateStatus;
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

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgramType type;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @Enumerated(EnumType.STRING)
    private OperateStatus operateStatus;

    @Builder
    private Program(Long id, String title, String subTitle, String content, float latitude, float longitude, OperateStatus operateStatus, ProgramType type, boolean deleted) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.operateStatus = operateStatus;
        this.type = type;
        this.deleted = deleted;
    }

    public static Program of(ProgramReq programReq) {
        return Program.builder()
                .title(programReq.getTitle())
                .subTitle(programReq.getSubTitle())
                .content(programReq.getContent())
                .latitude(programReq.getLatitude())
                .longitude(programReq.getLongitude())
                .operateStatus(OperateStatus.checkStatus(programReq.getStatus()))
                .type(ProgramType.handleType(programReq.getType()))
                .deleted(false)
                .build();
    }

    public void update(ProgramReq programReqDto) {
        this.title = programReqDto.getTitle();
        this.subTitle = programReqDto.getSubTitle();
        this.content = programReqDto.getContent();
        this.latitude = programReqDto.getLatitude();
        this.longitude = programReqDto.getLongitude();
        this.operateStatus = OperateStatus.checkStatus(programReqDto.getStatus());
        this.type = ProgramType.handleType(programReqDto.getType());
    }

    public void changeStatus() {
        if (this.operateStatus == OperateStatus.OPERATE) {
            this.operateStatus = OperateStatus.TERMINATE;
        } else {
            this.operateStatus = OperateStatus.OPERATE;
        }
    }

    public void deletedProgram() {
        this.deleted = true;
    }

    public void setImage(Image uploadImage) {
        this.image = uploadImage;
    }

    public void connectMember(Member member){
        this.member = member;
    }

    public void increaseViewCount(Long viewCount) {
        this.viewCount += viewCount;
    }

    public void decreaseViewCount(Long viewCount) {
        this.viewCount -= viewCount;
    }
}
