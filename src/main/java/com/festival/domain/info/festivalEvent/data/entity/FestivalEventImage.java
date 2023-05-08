package com.festival.domain.info.festivalEvent.data.entity;

import com.festival.domain.info.festivalPub.data.dto.request.PubRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String mainFilePath;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> subFilePaths = new ArrayList<>();

    @OneToOne(mappedBy = "festivalEventImage")
    private FestivalEvent festivalEvent;

    @Builder
    public FestivalEventImage(PubRequest pubRequest, String name, String type, String mainFilePath, List<MultipartFile> subFilePaths) throws IOException {
        this.name = name;
        this.type = type;
        this.mainFilePath = mainFilePath;
        for(MultipartFile subFile: subFilePaths)
            this.subFilePaths.add(subFile.getName());
    }
    public static FestivalEventImage of(MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException {
        return FestivalEventImage.builder()
                    .name("mainFile")
                    .mainFilePath(mainFile.getName())
                    .subFilePaths(subFiles)
                    .type(mainFile.getContentType())
                .build();
    }
}