package com.sparta.level3.repository;///

import com.sparta.level3.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface 	PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllByOrderByModifiedAtDesc();
	Optional<Post> findByUsername(String username);

	Post findByUserId(long userId);

	Optional<Post> findByUsernameAndId(String username, long id);

}
