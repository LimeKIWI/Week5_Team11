package com.example.intermediate.service;

import com.example.intermediate.controller.response.*;
import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final TokenProvider tokenProvider;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    @Transactional (readOnly = true)
    public ResponseDto<?> getMypage(HttpServletRequest request) {
        // 토큰 확인
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        // 리프레시 토큰을 이용해서 유저정보찾기
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }


        // 자신이 쓴 글 전부 가져와서 dto리스트로 저장.
        List<Post> postList = postRepository.findByMember(member);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        int count = 0;
        for(Post post : postList) {
//            count += post.getLike();  게시글에 있는 좋아요 수를 가져와서 저장
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .author(post.getMember().getNickname())
                            .content(post.getContent())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }


        // 자신이 쓴 댓글 전부 가져와서 dto리스트를 저장
        List<Comment> commentList = commentRepository.findByMember(member);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>(); // 댓글 저장할 dto 리스트 만들기
        count = 0;
        for (Comment comment : commentList) {
//            count += comment.getLike();  댓글에 있는 좋아요 수를 가져와서 저장 추후에 댓글 responsedto에 추가되어야함.
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }

        // 자신이 쓴 대댓글 전부 가져와서 dto리스트를 저장.

        //작성한 글,댓글,대댓글 responseDto를 만든다.
        MyActivityDto myActivityDto = MyActivityDto.builder()
                .nickName(member.getNickname())
                .postResponseDtoList(postResponseDtoList)
                .commentResponseDtoList(commentResponseDtoList)
                .build();


        // 좋아요기준으로 찾기 구현
        // 1. 좋아요 엔티티 테이블에서 findbyMember로 리스트를 불러온다.
        // 2. 한개의 FOR문을 반복시키며 ENUM값에 따라 다른 DtoList들에 정리하며 각 LIKE카운트를 올린다.(게시글/ 댓글/ 대댓글 각각의 좋아요수 집계를 위함)
        // 3. 최종 MyLikeDto에 3개의 dto리스트를 합친다.
        // 4. MyPageResponseDto에 myActivityDto와 MyLikeDto를 넣고 리턴.



        return ResponseDto.success(new MyPageResponseDto(myActivityDto));
    }
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
