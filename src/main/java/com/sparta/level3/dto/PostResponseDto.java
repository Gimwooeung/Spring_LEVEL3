package com.sparta.level3.dto;///


import com.sparta.level3.entity.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class PostResponseDto {
	private Long id;
	private String title;
	private String content;
	private String username;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public PostResponseDto (Post post) {
		this.id = post.getId();
		this.title = post.getTitle();
		this.content = post.getContent();
		this.username = post.getUsername();
		this.createdAt = post.getCreatedAt();
		this.modifiedAt = post.getModifiedAt();
	}
}
