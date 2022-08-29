package com.example.intermediate.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.intermediate.controller.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ImgUploadService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    private final AmazonS3Client s3Client;

    public ResponseDto<?> upload(MultipartFile multipartFile) throws IllegalAccessException {

        //파일 이름 변경
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        //파일 사이즈를 저장
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getSize());

        //aws s3에 전송
        try (InputStream inputStream = multipartFile.getInputStream()) {
            s3Client.putObject(bucket, s3FileName, inputStream, objMeta);
        } catch(IOException e) {
            throw new IllegalAccessException(String.format("파일 변환 중에러가 발생하였습니다.(%s)", multipartFile.getOriginalFilename()));
        }


        return ResponseDto.success(s3Client.getUrl(bucket, dir + s3FileName).toString());
    }
}
