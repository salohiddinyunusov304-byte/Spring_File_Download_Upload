package uz.pdp.entity;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BookCreateDto {
    private String title;
    private String description;
    private MultipartFile[] files;
}
