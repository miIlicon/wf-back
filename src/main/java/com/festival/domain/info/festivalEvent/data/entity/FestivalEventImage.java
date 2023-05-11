package com.festival.domain.info.festivalEvent.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalEventImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mainFileName;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> subFileNames = new ArrayList<>();

    @OneToOne(mappedBy = "festivalEventImage")
    private FestivalEvent festivalEvent;

    @Builder
    public FestivalEventImage(String mainFileName, List<String> subFileNames) throws IOException {
        this.mainFileName = mainFileName;
        this.subFileNames = subFileNames;
    }
    public static FestivalEventImage of(String mainFileName, List<String> subFileNames) throws IOException {
        return FestivalEventImage.builder()
                    .mainFileName(mainFileName)
                    .subFileNames(subFileNames)
                .build();
    }

    public void modify(String mainFilePath, List<String> subFileNames) {
        this.mainFileName = mainFilePath;
        this.subFileNames = subFileNames;
    }

    public void deleteOriginalFile(String filePath) {
        new File(filePath + this.mainFileName).delete();
        for(String subFileName: this.subFileNames)
            new File(filePath + subFileName).delete();
    }
}