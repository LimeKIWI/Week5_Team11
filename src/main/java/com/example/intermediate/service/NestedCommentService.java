package com.example.intermediate.service;

import com.example.intermediate.controller.request.CommentRequestDto;
import com.example.intermediate.controller.response.CommentResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.NestedComment;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.NestedCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NestedCommentService {

    private final NestedCommentRepository nestedCommentRepository;
    private final TokenProvider tokenProvider;
    private final CommentService commentService;
    @Transactional
    public ResponseDto<?> createNestedComment(CommentRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Comment comment = commentService.isPresentComment(requestDto.getPostId());
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        NestedComment nestedComment = NestedComment.builder()
                .member(member)
                .comment(comment)
                .content(requestDto.getContent())
                .numberOfLikes(0)
                .build();
        nestedCommentRepository.save(nestedComment);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(nestedComment.getId())
                        .author(nestedComment.getMember().getNickname())
                        .content(nestedComment.getContent())
                        .createdAt(nestedComment.getCreatedAt())
                        .modifiedAt(nestedComment.getModifiedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllNestedCommentByComment(Long commentId){
        Comment comment = commentService.isPresentComment(commentId);
        if(null == comment){
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        List<NestedComment> nestedCommentList = nestedCommentRepository.findAllByComment(comment);
        List<CommentResponseDto> nestedCommentResponseDtoList = new ArrayList<>();

        for(NestedComment nestedComment: nestedCommentList){
            nestedCommentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(nestedComment.getId())
                            .author(nestedComment.getMember().getNickname())
                            .content(nestedComment.getContent())
                            .createdAt(nestedComment.getCreatedAt())
                            .modifiedAt(nestedComment.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(nestedCommentResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> updateNestedComment(Long id, CommentRequestDto requestDto,HttpServletRequest request){
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Comment comment = commentService.isPresentComment(requestDto.getPostId());
        if(null == comment){
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        NestedComment nestedComment = isPresentNestedComment(id);
        if(null == nestedComment){
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 대댓글 id 입니다.");
        }
        if (nestedComment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        nestedComment.update(requestDto);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(nestedComment.getId())
                        .author(nestedComment.getMember().getNickname())
                        .content(nestedComment.getContent())
                        .createdAt(nestedComment.getCreatedAt())
                        .modifiedAt(nestedComment.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteNestedComment(Long id, HttpServletRequest request){
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        NestedComment nestedComment = isPresentNestedComment(id);
        if(null == nestedComment){
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 대댓글 id 입니다.");
        }

        if (nestedComment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        nestedCommentRepository.delete(nestedComment);
        return ResponseDto.success("success");
    }

    @Transactional(readOnly = true)
    public NestedComment isPresentNestedComment(Long id){
        Optional<NestedComment> optionalNestedComment = nestedCommentRepository.findById(id);
        return optionalNestedComment.orElse(null);

    }
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}

