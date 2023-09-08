package com.sparta.level3.repository;

import com.sparta.level3.entity.Comment;
import com.sparta.level3.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByOrderByModifiedAtDesc();
    List<Comment> findAllByPosts(Post post);

}