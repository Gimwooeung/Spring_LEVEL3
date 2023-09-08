package com.sparta.level3.repository;///

import com.sparta.level3.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface 	PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllByOrderByModifiedAtDesc();

}
