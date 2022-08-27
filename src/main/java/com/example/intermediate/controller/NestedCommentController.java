package com.example.intermediate.controller;

import com.example.intermediate.controller.request.CommentRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.NestedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/api/nestedComment/{id}", method = RequestMethod.GET)
    public ResponseDto<?> getAllNestedComments(@PathVariable Long id) {
        return nestedCommentService.getAllNestedCommentByComment(id);
    }

    @RequestMapping(value = "/api/auth/nestedComment/{id}", method = RequestMethod.PUT)
    public ResponseDto<?> updateNestedComment(@PathVariable Long id,@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return nestedCommentService.updateNestedComment(id, requestDto, request);
    }

    @RequestMapping(value = "/api/auth/nestedComment/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteNestedComment(@PathVariable Long id, HttpServletRequest request) {
        return nestedCommentService.deleteNestedComment(id, request);
    }






}
