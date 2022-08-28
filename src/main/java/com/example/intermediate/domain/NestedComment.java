package com.example.intermediate.domain;

import com.example.intermediate.controller.request.CommentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NestedComment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "comment_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;
    @Column(nullable = false)
    private int cnt;
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int countOfLikes;

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

    public  void like(){
        this.cnt +=1;
    }
    public  void dislike(){
        this.cnt -=1;
    }
}
