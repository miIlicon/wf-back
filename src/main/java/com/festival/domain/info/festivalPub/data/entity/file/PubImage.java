package com.festival.domain.info.festivalPub.data.entity.file;

import com.festival.domain.info.festivalPub.data.entity.pub.Pub;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PubImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pub_image_id")
    private Long id;

    @Column(name = "main_file_path", nullable = false)
    private String mainFilePath;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> subFilePaths = new ArrayList<>();

    @OneToOne(mappedBy = "pubImage")
    private Pub pub;

    public PubImage(String mainFilePath, Pub pub) {
        this.mainFilePath = mainFilePath;
        this.pub = pub;
    }

    public void saveSubFilePaths(List<String> subFilePath) {
        this.subFilePaths = subFilePath;
    }

    public void modifySubFilePaths(List<String> subFilePath) {
        for (String subFile : this.subFilePaths) {
            new File(subFile).delete();
        }
        this.subFilePaths = subFilePath;
    }

    public void modifyMainFilePath(String filePath, String mainFilePath, MultipartFile mainFile) throws IOException {
        new File(filePath + mainFilePath).delete();
        this.mainFilePath = mainFilePath;
        mainFile.transferTo(new File(filePath + mainFilePath));
    }
}
