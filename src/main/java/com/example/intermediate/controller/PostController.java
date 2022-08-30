package com.example.intermediate.controller;

import com.example.intermediate.controller.request.PostRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.PostService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class PostController {

  private final PostService postService;

  @RequestMapping(value = "/api/auth/post", method = RequestMethod.POST)
  public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
    return postService.createPost(requestDto, request);
  }

  @RequestMapping(value = "/api/auth/upload", method = RequestMethod.POST, consumes = {"multipart/form-data"})
  public ResponseDto<?> uploadImg(@RequestPart("file") MultipartFile file,  HttpServletRequest request) {
    return postService.uploadImg(file, request);
  }

  @RequestMapping(value = "/api/post/{id}", method = RequestMethod.GET)
  public ResponseDto<?> getPost(@PathVariable Long id) {
    return postService.getPost(id);
  }

  @RequestMapping(value = "/api/post", method = RequestMethod.GET)
  public ResponseDto<?> getAllPosts() {
    return postService.getAllPost();
  }

  @RequestMapping(value = "/api/auth/post/{id}", method = RequestMethod.PUT, consumes = {"multipart/form-data"})
  public ResponseDto<?> updatePost(@PathVariable Long id, @RequestPart("value") PostRequestDto postRequestDto, @RequestPart("file") MultipartFile file, HttpServletRequest request) {
    return postService.updatePost(id, postRequestDto, file, request);
  }

  @RequestMapping(value = "/api/auth/post/{id}", method = RequestMethod.DELETE)
  public ResponseDto<?> deletePost(@PathVariable Long id,
                                   HttpServletRequest request) {
    return postService.deletePost(id, request);
  }

}
