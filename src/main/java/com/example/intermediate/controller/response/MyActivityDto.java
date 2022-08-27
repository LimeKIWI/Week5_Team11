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
    private List<PostResponseDto> postResponseDtoList;
    private List<CommentResponseDto> commentResponseDtoList;
}
