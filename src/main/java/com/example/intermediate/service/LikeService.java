package com.example.intermediate.service;


import com.example.intermediate.controller.request.PostIdRequest;
import com.example.intermediate.controller.response.CommentResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Member;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    // private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> post_like(PostIdRequest postIdRequest ,HttpServletRequest request){

        Member member = validateMember(request);//현재 로그인중인 멤버
        Long id =postIdRequest.getPostId();//지금 현재 로그인 중이고 있는 애의 id를 가지고 있어야함



        return ResponseDto.success(member);
    }

    @Transactional
    public ResponseDto<?> post_dislike(Long id ,HttpServletRequest request){
        Member member = validateMember(request);//현재 로그인중인 멤버



        return ResponseDto.success(member);
    }


    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
