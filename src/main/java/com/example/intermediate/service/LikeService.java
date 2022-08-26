package com.example.intermediate.service;


import com.example.intermediate.controller.request.PostIdRequest;
import com.example.intermediate.controller.response.CommentResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Member;
import com.example.intermediate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<?> post_like(PostIdRequest postIdRequest ){
        Long id =postIdRequest.getPostId();
        Optional<Member> member = memberRepository.findById(id);
        Member temp = member.get();
        temp.like_post(id);
        return ResponseDto.success(member);
    }
}
