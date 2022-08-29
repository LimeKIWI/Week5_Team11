package com.example.intermediate.domain;

import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.PostRepository;
import com.example.intermediate.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostService postService;


    @Scheduled(cron = " 0 1 * * * *") //매일 새벽 1시에 업데이트
    public void updatePostByComment() throws InterruptedException {
        log.info("게시물 업데이트 실행");
        List<Post> postList = postRepository.findAll();
        if (postList.size() != 0) { //게시물 존재 시
            for (long id = postList.get(0).getId(); id <= postList.get(postList.size() - 1).getId(); id++) {
                //postlist의 첫 번째 id값 부터 마지막까지 반복
                Post idd = postService.isPresentPost(id);
                if (idd != null) { //id가 사라져서 null이 들어와도 commentCount.size가 0이되니 이를 방지
                    List<Comment> commentCount = commentRepository.findAllByPost(idd);//post의 값이 일치한 걸 찾음

                    if (commentCount.size() == 0) {
                        log.info("게시물 " + postRepository.findById(id).get().getTitle() + "이 삭제되었습니다.");
                        postRepository.deleteById(id);
                    }
                }
            }
        }

    }


}


