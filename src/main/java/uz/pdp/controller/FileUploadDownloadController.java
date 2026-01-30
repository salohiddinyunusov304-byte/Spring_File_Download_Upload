package uz.pdp.controller;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.dao.UploadDao;
import uz.pdp.entity.BookCreateDto;
import uz.pdp.entity.Upload;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
public class FileUploadDownloadController {
    private final Path rootPath = Path.of("/Users/salohiddinyunusov/IdeaProjects/spring_introduction/spring_security_3/upload");
    private final UploadDao uploadDao;

    public FileUploadDownloadController(UploadDao uploadDao) {
        this.uploadDao = uploadDao;
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploading(@ModelAttribute BookCreateDto dto) throws IOException {
        System.out.println("Dto: " + dto);

        for (MultipartFile file : dto.getFiles()) {
            String originalFilename = file.getOriginalFilename();
            System.out.println("OriginalName: " + originalFilename);
            String generatedName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(originalFilename);
            System.out.println("GeneratedName: " + generatedName);
            Path path = rootPath.resolve(generatedName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            uploadDao.save(Upload.builder()
                    .originalFilename(originalFilename)
                    .generateFilename(generatedName)
                    .mimeType(file.getContentType())
                    .size(file.getSize())
                    .build());
        }
        return "redirect:/upload";
    }

    @GetMapping("/download/{generateFilename:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable(name = "generateFilename") String generateFilename) throws MalformedURLException {
        Upload upload = uploadDao.findByGeneratedFileName(generateFilename);
        Path path = rootPath.resolve(upload.getGenerateFilename());
        UrlResource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment: filename=\"" + upload.getGenerateFilename() + "\"")
                .header("Content-Type", upload.getMimeType())
                .body(resource);
    }

}
