package com.example.intermediate.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service

public class ImgUploadService {
    @Value("${cloud.aws.s3.bucket}")
    private  String bucket;
    @Value("${cloud.aws.s3.dir}")
    private  String dir;

    private final AmazonS3Client s3Client;
    private final PostService postService;
    @Transactional
    public ResponseDto<?> upload(MultipartFile multipartFile,Long id) throws IllegalAccessException{
        Post post = postService.isPresentPost(id);
        if(null==post){
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        if (!post.getFileName().equals("x")) {
            //이미 채워진 상태
            s3Client.deleteObject(bucket,post.getFileName());
            //지우고 다시 생성!
        }
        String s3FileName = UUID.randomUUID()+"-"+multipartFile.getOriginalFilename();//filename  초기화
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());
        try(InputStream inputStream = multipartFile.getInputStream()){
            s3Client.putObject(bucket,s3FileName,inputStream,objectMetadata);
        }catch (IOException e){
            return ResponseDto.fail("fail","파일변환에 실패했습니다");
        }
        //해당 이미지가 s3에 저장이 된 상태
        String url = s3Client.getUrl(bucket,dir+s3FileName).toString();
        post.add_url(url,s3FileName);
        return  ResponseDto.success(url);

    }



}
