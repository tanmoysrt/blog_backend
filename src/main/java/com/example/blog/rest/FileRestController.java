package com.example.blog.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileRestController {

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        // Write file in `uploads` folder with random name and also preserve extension
        String filename = UUID.randomUUID() + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(new File(System.getProperty("user.dir")+"/uploads/" + filename));
        return filename;
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        // Read file from `uploads` folder
        String filePath = System.getProperty("user.dir")+"/uploads/" + filename;
        Path filePathPath = Path.of(filePath);
        byte[] fileContent = Files.readAllBytes(filePathPath);
        Resource fileResource = new ByteArrayResource(fileContent);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(Files.probeContentType(filePathPath)))
                .body(fileResource);
    }


    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
