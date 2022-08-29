package com.example.intermediate.repository;

import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.NestedComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NestedCommentRepository extends JpaRepository<NestedComment, Long> {
    List<NestedComment> findAllByComment(Comment comment);
    List<NestedComment> findByMember(Member member);

}
