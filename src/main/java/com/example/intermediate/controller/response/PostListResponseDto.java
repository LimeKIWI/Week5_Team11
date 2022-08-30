package com.example.intermediate.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponseDto {
  private Long id;
  private String title;
  private String author;
  private int countOfComments;
  private int countOfLikes;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
