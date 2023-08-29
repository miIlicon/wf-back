package com.festival.domain.image.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mainFilePath;

    @ElementCollection
    private List<String> subFilePaths;

    @Builder
    private Image(String mainFilePath, List<String> subFilePaths) {
        this.mainFilePath = mainFilePath;
        this.subFilePaths = subFilePaths;
    }
}
