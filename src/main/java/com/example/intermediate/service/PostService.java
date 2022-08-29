package com.example.intermediate.service;

import com.example.intermediate.controller.response.CommentResponseDto;
import com.example.intermediate.controller.response.PostResponseDto;
import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.ImageMapper;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import com.example.intermediate.controller.request.PostRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AmazonS3Service amazonS3Service;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, MultipartFile file, HttpServletRequest request) {
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

        // 만약 파일매핑 테이블을 사용하지 않는다면 url로 반환값을 받고 Post에 url를 저장하며 추후 파일 이름이 필요한 부분에서 substring을 활용해 뒷부분의 파일이름만 잘라내면된다.
        // 위의 방식은 데이터베이스공간낭비가 적어지며 코드가 단순해진다. 매번 데이터베이스에서 정보를 불러올 일도 사라짐.
        // 테이블생성 방식을 사용한 이유는 다음과 같다.
        // 1. 업로드된 파일의 관리를 쉽게하기 위하여.
        // 2. 파일이름 생성을 파일이름 추가없이 전체 난수로 돌릴 경우등의 수를 대비하여
        // 3. 누군가가 내부 코드를 알고있다면 substring등의 전체 url에서 파일이름만을 제외하는 방식을 악용하여 자신이 소유하지 않은 파일을 삭제하려 시도할 수 있음.
        ImageMapper imageMapper = amazonS3Service.uploadFile(file); // s3 파일업로드

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .image(imageMapper)
                .countOfLikes(0)
                .member(member)
                .build();
        postRepository.save(post);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .author(post.getMember().getNickname())
                        .image(post.getImage().getUrl())
                        .countOfLikes(post.getCountOfLikes())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(Long id) {
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .countOfLikes((comment.getCountOfLikes()))
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .image(post.getImage().getUrl())
                        .countOfLikes(post.getCountOfLikes())
                        .commentResponseDtoList(commentResponseDtoList)
                        .author(post.getMember().getNickname())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPost() {
        return ResponseDto.success(postRepository.findAllByOrderByModifiedAtDesc());
    }

    @Transactional
    public ResponseDto<Post> updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
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

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        post.update(requestDto);
        return ResponseDto.success(post);
    }

    @Transactional
    public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
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

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        // 만약 매핑테이블을 사용하지 않는다면 바로 url에서 파일이름을 빼오면 된다.

//        String imageName = post.getImage().getUrl().substring(post.getImage().getUrl().lastIndexOf("com/")+4);
//        System.out.println(imageName);
//        System.out.println(post.getImage().getName());

        if (!amazonS3Service.removeFile(post.getImage().getName()))
            return ResponseDto.fail("BAD_REQUEST", "삭제 오류 발생.");
        postRepository.delete(post);
        return ResponseDto.success("delete success");
    }

    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
