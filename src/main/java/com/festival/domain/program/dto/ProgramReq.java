package com.festival.domain.program.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgramReq {

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
    private String status;

    @NotNull(message = "타입을 입력해주세요.")
    private String type;

    public ProgramReq(String title, String subTitle, String content, float latitude, float longitude, String status) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

}
