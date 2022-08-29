package com.example.intermediate.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyActivityDto {
    private String nickName;
    private int countOfLikeByPostAll;
    private List<PostResponseDto> postResponseDtoList;
    private int countOfLikeByCommentAll;
    private List<CommentResponseDto> commentResponseDtoList;
    private int countOfLikeByNestCommentAll;
    private List<CommentResponseDto> nestedCommentResponseDtoList;
}
