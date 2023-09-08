package com.sparta.level3.controller;

import com.sparta.level3.dto.CommentDeleteDto;
import com.sparta.level3.dto.CommentRequestDto;
import com.sparta.level3.dto.CommentResponseDto;
import com.sparta.level3.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    // 1. 댓글 작성
    @PostMapping("/comment/{id}")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, HttpServletRequest request, @PathVariable long id) {
        return commentService.createComment(requestDto, request, id);
    }

    // 2. 댓글 수정
    @PutMapping("/comment/{id}")
    public CommentResponseDto update(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.update(id, requestDto, request);
    }

    // 3. 댓글 삭제
    @DeleteMapping("/comment/{id}")
    public CommentDeleteDto delete(@PathVariable Long id, HttpServletRequest request) {
        return commentService.delete(id, request);
    }
}