package com.festival.domain.info.festivalPub.data.entity.file;

import com.festival.domain.info.festivalPub.data.dto.request.PubRequest;
import com.festival.domain.info.festivalPub.data.entity.pub.Pub;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PubImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "main_file_path", nullable = false)
    private String mainFilePath;

    @OneToMany(mappedBy = "image", cascade = CascadeType.REMOVE)
    private List<SubFilePath> subFilePaths = new ArrayList<>();

    @OneToOne(mappedBy = "pubImage")
    private Pub pub;

    public PubImage(PubRequest pubRequest, String name, String mainFilePath, Pub pub) throws IOException {
        this.name = name;
        this.type = pubRequest.getMainFile().getContentType();
        this.mainFilePath = mainFilePath;
        this.pub = pub;
    }

    public void setSubFilePath(List<SubFilePath> subFilePath) {
        this.subFilePaths = subFilePath;
    }
}
