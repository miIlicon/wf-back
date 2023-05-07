package com.festival.domain.foodTruck.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodTruckRequest implements Serializable {
    private static final long serialVersionUID = 1L;
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

    @NotNull(message = "상태를 입력해주세요.")
    private Boolean foodTruckState;
}
