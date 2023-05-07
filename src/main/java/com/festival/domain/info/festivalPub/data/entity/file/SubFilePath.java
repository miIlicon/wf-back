package com.festival.domain.info.festivalPub.data.entity.file;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubFilePath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    private PubImage image;

    public SubFilePath(String name, String type, String filePath, PubImage image) {
        this.name = name;
        this.type = type;
        this.filePath = filePath;
        this.image = image;
    }
}
