package com.example.intermediate.controller;

import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.ImgUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
public class ImgUploadController {
    private final ImgUploadService imguploadService;

    @PostMapping("/upload")
    public ResponseDto<?> uploadFile(@RequestParam("images") MultipartFile multipartFile)
            throws IllegalAccessException {
        return imguploadService.upload(multipartFile);

    }
}
