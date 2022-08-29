package com.example.intermediate.domain;

import com.example.intermediate.controller.request.PostRequestDto;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;
  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private int countOfLikes;

  @OneToOne
  private ImageMapper image;

  public void update(PostRequestDto postRequestDto) {
    this.title = postRequestDto.getTitle();
    this.content = postRequestDto.getContent();
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

  public void addComment(Comment comment) {
    comments.add(comment);
    comment.setPost(this);
  }

  public void removeCommentList(Comment comment) {
    comments.remove(comment);
    comment.setPost(this);
  }
}
