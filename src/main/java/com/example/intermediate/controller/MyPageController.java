package com.example.intermediate.controller;

import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    // 마이페이지 불러오기
    @RequestMapping(value = "/api/auth/member/mypage", method = RequestMethod.GET)
    public ResponseDto<?> getMypage(HttpServletRequest request) {
        return myPageService.getMypage(request);
    }
}
