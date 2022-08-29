package com.example.intermediate.domain;

import com.example.intermediate.controller.request.CommentRequestDto;

import javax.persistence.*;

import lombok.*;

import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @JoinColumn(name = "post_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Post post;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<NestedComment> nestedComments;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private int countOfLikes;

  public void update(CommentRequestDto commentRequestDto) {
    this.content = commentRequestDto.getContent();
  }

  public  void like(){
    this.countOfLikes +=1;
  }
  public  void dislike(){
    this.countOfLikes -=1;
  }

  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }

  public void addNestComment(NestedComment nestedComment) {
    nestedComments.add(nestedComment);
    nestedComment.setComment(this);
  }

  public void removeNestCommentList(NestedComment nestedComment) {
    nestedComments.remove(nestedComment);
    nestedComment.setComment(this);
  }
}
