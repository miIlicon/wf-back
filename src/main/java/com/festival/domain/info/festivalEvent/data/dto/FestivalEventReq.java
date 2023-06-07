package com.festival.domain.info.festivalEvent.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalEventReq {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "소제목을 입력해주세요.")
    private String subTitle;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "위치를 입력해주세요.")
    private Float latitude;

    @NotNull(message = "위치를 입력해주세요.")
    private Float longitude;

    @NotNull(message = "상태를 입력해주세요.")
    private Boolean state;

    @Builder
    public FestivalEventReq(String title, String subTitle, String content, MultipartFile mainFile, List<MultipartFile> subFiles, float latitude, float longitude, Boolean state) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
    }

    public static FestivalEventReq of(FestivalEventDto festivalEventDto){
        return  FestivalEventReq.builder()
                .title(festivalEventDto.getTitle())
                .subTitle(festivalEventDto.getSubTitle())
                .content(festivalEventDto.getContent())
                .latitude(festivalEventDto.getLatitude())
                .longitude(festivalEventDto.getLongitude())
                .state(festivalEventDto.getState())
                .build();
    }
}
