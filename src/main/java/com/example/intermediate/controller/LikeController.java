package com.example.intermediate.controller;


import com.example.intermediate.controller.request.CommentRequestDto;
import com.example.intermediate.controller.request.PostIdRequest;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@Controller
public class LikeController {


    final private LikeService likeService;
    @PostMapping(value = "/api/auth/post/like")
    public ResponseDto<?> post_like(@RequestBody PostIdRequest postIdRequest) {
       return  likeService.post_like(postIdRequest);
    }

}
