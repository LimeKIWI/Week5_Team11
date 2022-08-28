package com.example.intermediate.controller.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
    private MyActivityDto myActivityDto;
    private MyLikesDto myLikesDto;
}
