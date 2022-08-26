package com.example.intermediate.controller.response;


import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
public class MyPageResponseDto {
    private MyActivityDto myActivityDto;

    public MyPageResponseDto (MyActivityDto myActivityDto) {
        this.myActivityDto = myActivityDto;
    }
}
