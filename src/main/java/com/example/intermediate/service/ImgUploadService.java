package com.example.intermediate.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.intermediate.controller.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImgUploadService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    private final AmazonS3Client s3Client;

    public ResponseDto<?> upload(InputStream inputStream, String originFileName, String fileSize) {

        //파일 이름 변경
        String s3FileName = UUID.randomUUID() + "-" + originFileName;

        //파일 사이즈를 저장
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(Long.parseLong(fileSize));

        //aws s3에 전송
        s3Client.putObject(bucket, s3FileName, inputStream, objMeta);

        return ResponseDto.success(s3Client.getUrl(bucket, dir + s3FileName).toString());
    }
}
