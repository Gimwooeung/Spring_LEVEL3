package com.sparta.level3.service;

import com.sparta.level3.dto.CommentResponseDto;
import com.sparta.level3.dto.DeleteReponseDto;
import com.sparta.level3.dto.PostRequestDto;
import com.sparta.level3.dto.PostResponseDto;
import com.sparta.level3.entity.Comment;
import com.sparta.level3.entity.Post;
import com.sparta.level3.entity.User;
import com.sparta.level3.jwt.JwtUtil;
import com.sparta.level3.repository.CommentRepository;
import com.sparta.level3.repository.PostRepository;
import com.sparta.level3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    // 1. 토큰 있는 경우에만 게시글 생성
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
          // 사용자의 정보 가져오기. request에서 Token가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 토큰이 있는 경우에만 글 작성 가능
        if (token != null) {
            // 토큰 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            }else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회 -> 로그인 안했으면 로그인 하라고 메시지
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("로그인 해주세요")
            );

            Post post = new Post(requestDto, user.getUsername());

            post.setUser(user);
            postRepository.save(post);
            return new PostResponseDto(post);
        }else {
            return null;
        }
    }
    // 2. 게시글 전체 목록 조회
    @Transactional
    public List<PostResponseDto> getPostList() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        if(Collections.isEmpty(posts)) return null;
        List<PostResponseDto> results = new ArrayList<>();

        for(Post post : posts) {
            List<Comment> comments = commentRepository.findAllByPosts(post);
            PostResponseDto postResponseDto = new PostResponseDto(post);
            List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
            for (Comment comment : comments) {
                commentResponseDtos.add(new CommentResponseDto(comment));
            }
            postResponseDto.setCommentList(commentResponseDtos);
            results.add(postResponseDto);
        }
        return results;
    }

    // 3. 선택한 게시글 조회 -> 예외처리("게시글이 존재하지 않습니다")
    @Transactional
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow( // Post 꺼내오기
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        PostResponseDto postDto = new PostResponseDto(post); // PostResponseDto 빈통 만들기 (주연)

        List<Comment> comments = commentRepository.findAllByPosts(post);
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentResponseDtos.add(new CommentResponseDto(comment));
        }
        postDto.setCommentList(commentResponseDtos);
        return postDto;
    }

    // 4. 선택한 게시글 수정 -> ("아이디가 존재하지 않습니다")
    @Transactional
    public PostResponseDto update(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 사용자의 정보 가져오기. request에서 Token가져오기
        if (token != null) {
            // 토큰 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            }else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회 -> 로그인 안했으면 로그인 하라고 메시지
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("로그인 해주세요")
            );
            Post post = postRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")
            );

            if(!post.getUsername().equals(user.getUsername())){
                throw new IllegalArgumentException("해당 게시물에 권한이 존재하지 않습니다.");
            }

            post.update(requestDto);
            postRepository.save(post);
            return new PostResponseDto(post);
        }else {
            return null;
        }
    }

    // 5. 선택한 게시글 삭제
    @Transactional
    public DeleteReponseDto delete(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 사용자의 정보 가져오기. request에서 Token가져오기
        if (token != null) {
            // 토큰 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            }else {
                throw new IllegalArgumentException("Token Error");
            }
            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회 -> 로그인 안했으면 로그인 하라고 메시지
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("로그인 해주세요")
            );

            //조회
            Post post = postRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")
            );

            if(!post.getUsername().equals(user.getUsername())){
                throw new IllegalArgumentException("해당 게시물에 권한이 존재하지 않습니다.");
            }

            //삭제
            postRepository.deleteById(id);
            return new DeleteReponseDto("게시글 삭제 성공",200);
        }
        return null;
    }
}