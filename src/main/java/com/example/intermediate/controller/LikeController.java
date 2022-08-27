package com.example.intermediate.controller;


import com.example.intermediate.controller.request.ParentIdRequest;

import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@Component
@RestController
public class LikeController {
    private final LikeService likeService;
    @RequestMapping(value = "/api/auth/post/like", method = RequestMethod.POST)//현재 가져가서 ilike!
    public ResponseDto<?>  like_post(@RequestBody ParentIdRequest parentRequest, HttpServletRequest request) {
        return likeService.post_like(parentRequest, request);
    }

    @RequestMapping(value = "/api/auth/post/dislike/{id}", method = RequestMethod.PATCH)
    public ResponseDto<?> dislike_post(@PathVariable Long id, @RequestBody ParentIdRequest parentRequest , HttpServletRequest request) {
        return likeService.post_dislike(id,parentRequest,request);
    }

    @RequestMapping(value = "/api/auth/comment/like", method = RequestMethod.POST)//현재 가져가서 ilike!
    public ResponseDto<?>  like_comment(@RequestBody ParentIdRequest parentRequest, HttpServletRequest request) {
        return likeService.like_comment(parentRequest, request);
    }

    @RequestMapping(value = "/api/auth/comment/dislike/{id}", method = RequestMethod.PATCH)
    public ResponseDto<?> dislike_comment(@PathVariable Long id, @RequestBody ParentIdRequest parentRequest , HttpServletRequest request) {
        return likeService.comment_dislike(id,parentRequest,request);
    }
}
