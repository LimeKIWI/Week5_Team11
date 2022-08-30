package com.example.intermediate.controller;

import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.ImgUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@RestController
public class ImgUploadController {
    private final ImgUploadService imguploadService;

    @PostMapping("/auth/upload")
    public ResponseDto<?> uploadFile(@RequestParam("images") MultipartFile multipartFile, HttpServletRequest request)
            throws IllegalAccessException {
        return imguploadService.upload(multipartFile,request);

    }
}
