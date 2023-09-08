package com.sparta.level3.entity;

import com.sparta.level3.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post posts;

    public Comment(CommentRequestDto requestDto, String username) {
        this.content = requestDto.getContent();
        this.username = username;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.username = this.user.getUsername();
    }

    public void setPostAndUser(Post posts, User user) {
        this.posts = posts;
        this.user = user;
        posts.getCommentList().add(this);
    }
}