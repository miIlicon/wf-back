package com.festival.domain.info.festivalEvent.data.entity;

import jakarta.persistence.*;
import lombok.*;

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
        for(String subFileName: subFileNames)
            this.subFileNames.add(subFileName);
    }
    public static FestivalEventImage of(String mainFileName, List<String> subFileNames) throws IOException {
        return FestivalEventImage.builder()
                    .mainFileName(mainFileName)
                    .subFileNames(subFileNames)
                .build();
    }

    public void modify(String mainFilePath, List<String> subFileNames) {
        System.out.println("orginal fileNames: " + getSubFileNames());
        this.mainFileName = mainFilePath;
        this.subFileNames = subFileNames;
        System.out.println("modify fileNames" + getSubFileNames());
    }
}