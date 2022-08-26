package com.example.intermediate.controller;


import com.example.intermediate.controller.request.CommentRequestDto;
import com.example.intermediate.controller.request.PostIdRequest;
import com.example.intermediate.controller.request.PostRequestDto;
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
    @RequestMapping(value = "api/auth/post/like", method = RequestMethod.POST)
    public ResponseDto<?> like_post(@RequestBody PostIdRequest postIdRequest, HttpServletRequest request) {
        System.out.println(postIdRequest.getPostId());
        return likeService.post_like(postIdRequest,request);
    }

    @RequestMapping(value = "api/auth/post/dislike/{id}", method = RequestMethod.POST)
    public ResponseDto<?> dislike_post(@PathVariable Long id, HttpServletRequest request) {
        return likeService.post_dislike(id,request);
    }
}
