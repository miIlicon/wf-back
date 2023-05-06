package com.festival.domain.info.festivalPub.data.entity.file;

import com.festival.domain.info.festivalPub.data.dto.reqest.PubRequest;
import com.festival.domain.info.festivalPub.data.entity.pub.FestivalPub;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

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

    @OneToMany(mappedBy = "image")
    private List<FilePath> subFilePaths = new ArrayList<>();

    @OneToOne(mappedBy = "pubImage")
    private FestivalPub festivalPub;

    public static PubImage of(PubRequest pubRequest) throws IOException {  // pub 임시

        String mainFileName = createStoreFileName(pubRequest.getMainFile().getOriginalFilename());



        file.transferTo(new java.io.File(filePath + storeFileName));

        return PubImage.builder()
                .name(storeFileName)
                .type(file.getContentType())
                .mainFilePath(filePath + storeFileName)
                .subFilePaths(subFilePaths)
                .festivalPub(pub)
                .build();
    }

    private static String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private static String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
