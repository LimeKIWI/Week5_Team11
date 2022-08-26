package com.example.intermediate.controller;

import com.example.intermediate.controller.request.CommentRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.NestedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class NestedCommentController {
    private final NestedCommentService nestedCommentService;

    @RequestMapping(value = "/api/auth/nestedComment", method = RequestMethod.POST)
    public ResponseDto<?> createNestedComment(@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return nestedCommentService.createNestedComment(requestDto, request);
    }

}
