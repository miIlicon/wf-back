package com.festival.domain.fleaMarket.data.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FleaMarketImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_image_id")
    private Long id;

    @Column(name = "main_file_name", nullable = false)
    private String mainFileName;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "market_sub_image", joinColumns = @JoinColumn(name = "market_image_id"))
    @Column(name = "file_name")
    private List<String> subFileNames = new ArrayList<>();

    @OneToOne(mappedBy = "marketImage", fetch = FetchType.LAZY)
    private FleaMarket fleaMarket;

    public FleaMarketImage(String mainFilePath, FleaMarket fleaMarket) {
        this.mainFileName = mainFilePath;
        this.fleaMarket = fleaMarket;
    }

    public void saveSubFileNames(List<String> subFileNames) {
        this.subFileNames = subFileNames;
    }
}