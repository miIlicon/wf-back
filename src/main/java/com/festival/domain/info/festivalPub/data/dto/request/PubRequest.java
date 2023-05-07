package com.festival.domain.info.festivalPub.data.dto.request;

import com.festival.domain.info.festivalPub.data.entity.pub.PubState;
import com.festival.global.customAnnotation.customEnum.Enum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PubRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "소제목을 입력해주세요.")
    private String subTitle;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private MultipartFile mainFile;

    private List<MultipartFile> subFiles;

    @NotNull(message = "위치를 입력해주세요.")
    private int latitude;

    @NotNull(message = "위치를 입력해주세요.")
    private int longitude;

    @Enum(message = "상태를 입력해주세요.", enumClass = PubState.class, ignoreCase = true)
    private String pubState;

    public PubRequest(String title, String subTitle, String content, int latitude, int longitude, String pubState) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pubState = pubState;
    }

    public PubRequest(String title, String subTitle, String content, MultipartFile mainFile, List<MultipartFile> subFiles, int latitude, int longitude, String pubState) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.mainFile = mainFile;
        this.subFiles = subFiles;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pubState = pubState;
    }
}
