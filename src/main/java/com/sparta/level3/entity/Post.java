package com.sparta.level3.entity;

import com.sparta.level3.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
public class Post extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "contents", nullable = false)
	private String content;


	@OneToMany(mappedBy = "posts")
	private List<Comment> commentList = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	// @Entity 어노테이션 붙은 클래스는 기본생성자가 필수!!
	public Post(PostRequestDto requestDto, String username) {
		this.title = requestDto.getTitle();
		this.content = requestDto.getContent();
		this.username = username;
	}

	public void update(PostRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.content = requestDto.getContent();
	}

	// user를 받아서 user에 post를 세팅하기
	public void setUser(User user) {
		this.user = user;
		user.getPosts().add(this);
	}
}