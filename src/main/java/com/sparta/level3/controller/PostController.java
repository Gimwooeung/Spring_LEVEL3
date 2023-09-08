package com.sparta.level3.controller;


import com.sparta.level3.dto.DeleteReponseDto;
import com.sparta.level3.dto.PostRequestDto;
import com.sparta.level3.dto.PostResponseDto;
import com.sparta.level3.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
	private final PostService postService;

	// 1. 게시글 생성
	@PostMapping("/post")
	public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
		return postService.createPost(requestDto, request);
	}
	// 2. 게시글 전체 목록 조회
	@GetMapping("/post")
	public List<PostResponseDto> getPostList(){
		return postService.getPostList();
	}
	// 3. 선택한 게시글 조회
	@GetMapping("/post/{id}")
	public PostResponseDto getPost(@PathVariable Long id) {
		return postService.getPost(id);
	}
	// 4. 선택한 게시글 수정
	@PutMapping("/post/{id}")
	public PostResponseDto update(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request ) {
		return postService.update(id, requestDto, request);
	}
	// 5. 선택한 게시글 삭제
	@DeleteMapping("/post/{Id}")
	public DeleteReponseDto delete(@PathVariable Long Id, HttpServletRequest request) {
		return postService.delete(Id, request);
	}
}
