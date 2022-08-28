package com.example.intermediate.service;


import com.example.intermediate.controller.request.ParentIdRequest;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.*;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final PostRepository  postRepository;
    private final LikeRepository likeRepository;
    private final TokenProvider tokenProvider;
    private final CommentRepository commentRepository;
    private NestedCommentRepository nestedCommentRepository;

    @Transactional
    public ResponseDto<?> post_like(ParentIdRequest parentRequest, HttpServletRequest request){
        Member member = validateMember(request);//현재 로그인중인 멤버
        Optional<Post> temp = postRepository.findById(parentRequest.getParentId());
        if (!temp.isPresent()) {
            return ResponseDto.fail("fail-like", "해당 게시글이 존재하지 않습니다.");
        }
        Post  post = temp.get();
        post.like();
        Like like = Like.builder()
                .member(member)
                .pid(post.getId())//
                .role(Role_Enum.POST)
                .build();
        likeRepository.save(like);
        return ResponseDto.success("좋아요!");
    }

    @Transactional
    public ResponseDto<?> post_dislike(Long id , ParentIdRequest parentRequest, HttpServletRequest request){
        Member member = validateMember(request);//현재 로그인중인 회원의 아이디

        Optional<Like> temp = likeRepository.findByIdAndPidAndRole(id,parentRequest.getParentId(),Role_Enum.POST);
        if (!temp.isPresent()) {
            return ResponseDto.fail("fail-dislike", "해당 좋아요가 존재하지 않습니다.");
        }
        Optional<Post> temp2 = postRepository.findById(parentRequest.getParentId());
        if (!temp2.isPresent()) {
            return ResponseDto.fail("fail-dislike", "해당 게시물이 존재하지 않습니다.");
        }
        Like like = temp.get();
        if(!Objects.equals(like.getMember().getId(), member.getId())){
            return ResponseDto.fail("fail-dislike", "해당 좋아요의 작성자가 아닙니다.");
        }

        //해당 로그인한 유저가 해당 댓글의 작성자가 아닐 경우에는 예외처리를 해야함
        Post post = temp2.get();
        if(!Objects.equals(like.getPid(), post.getId())){
            return ResponseDto.fail("fail-dislike", "해당 게시글의 좋아요가 아닙니다.");
        }
        post.dislike();

        likeRepository.delete(like);
        return ResponseDto.success("성공");
    }

    @Transactional
    public ResponseDto<?> comment_like(LikeIdRequest likeIdRequest, HttpServletRequest request){
        Member member = validateMember(request);//현재 로그인중인 멤버
        Optional<Comment> temp = commentRepository.findById(parentRequest.getParentId());
        if (!temp.isPresent()) {
            return ResponseDto.fail("fail-like", "해당 댓글이 존재하지 않습니다.");
        }
        Comment comment = temp.get();

        comment.like();
        Like like = Like.builder()
                .member(member)
                .pid(comment.getId())//
                .role(Role_Enum.COMMENT)
                .build();
        likeRepository.save(like);
        return ResponseDto.success("좋아요!");
    }

    @Transactional
    public ResponseDto<?> comment_dislike(Long id , ParentIdRequest parentRequest, HttpServletRequest request){
        Member member = validateMember(request);//현재 로그인중인 회원의 아이디
        Optional<Like> temp = likeRepository.findByIdAndPidAndRole(id,parentRequest.getParentId(),Role_Enum.COMMENT);
        if (!temp.isPresent()) {
            return ResponseDto.fail("fail-dislike", "해당 좋아요가 존재하지 않습니다.");
        }
        Optional<Comment> temp2 = commentRepository.findById(parentRequest.getParentId());
        if (!temp2.isPresent()) {
            return ResponseDto.fail("fail-dislike", "해당 댓글이 존재하지 않습니다.");
        }
        Like like = temp.get();
        if(!Objects.equals(like.getMember().getId(), member.getId())){
            return ResponseDto.fail("fail-dislike", "해당 좋아요의 작성자가 아닙니다.");
        }
        //해당 로그인한 유저가 해당 댓글의 작성자가 아닐 경우에는 예외처리를 해야함
        Comment comment= temp2.get();
        if(!Objects.equals(like.getPid(), comment.getId())){
            return ResponseDto.fail("fail-dislike", "해당 댓글의 좋아요가 아닙니다.");
        }
        comment.dislike();

        likeRepository.delete(like);
        return ResponseDto.success("좋아요 취소!");
    }
    @Transactional
    public ResponseDto<?> nestedcomment_like(LikeIdRequest likeIdRequest, HttpServletRequest request){
        Member member = validateMember(request);//현재 로그인중인 멤버
        Optional<NestedComment> temp = nestedCommentRepository.findById(likeIdRequest.getLikeId());
        if (!temp.isPresent()) {
            return ResponseDto.fail("fail-like", "해당 대댓글이 존재하지 않습니다.");
        }
        NestedComment nestedComment = temp.get();

        nestedComment.like();
        Like like = Like.builder()
                .member(member)
                .pid(nestedComment.getId())//
                .role(Role_Enum.COMMENT)
                .build();
        nestedComment.like();
        likeRepository.save(like);
        return ResponseDto.success("좋아요!");
    }

    @Transactional
    public ResponseDto<?> nestedcomment_dislike(Long id , ParentIdRequest parentRequest, HttpServletRequest request){
        Member member = validateMember(request);//현재 로그인중인 회원의 아이디
        Optional<Like> temp = likeRepository.findByIdAndPidAndRole(id,parentRequest.getParentId(),Role_Enum.COMMENT);
        if (!temp.isPresent()) {
            return ResponseDto.fail("fail-dislike", "해당 좋아요가 존재하지 않습니다.");
        }
        Optional<NestedComment> temp2 = nestedCommentRepository.findById(parentRequest.getParentId());
        if (!temp2.isPresent()) {
            return ResponseDto.fail("fail-dislike", "해당 대댓글이 존재하지 않습니다.");
        }
        Like like = temp.get();
        if(like.getMember().getId()!=member.getId()){
            return ResponseDto.fail("fail-dislike", "해당 좋아요의 작성자가 아닙니다.");
        }
        //해당 로그인한 유저가 해당 댓글의 작성자가 아닐 경우에는 예외처리를 해야함
        NestedComment nestedComment= temp2.get();
        if(like.getPid()!=nestedComment.getId()){
            return ResponseDto.fail("fail-dislike", "해당 댓글의 좋아요가 아닙니다.");
        }
        nestedComment.dislike();
        likeRepository.delete(like);
        return ResponseDto.success("좋아요 취소!");
    }


    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
