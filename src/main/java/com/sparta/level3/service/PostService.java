package com.sparta.level3.service;///

import com.sparta.level3.dto.DeleteReponseDto;
import com.sparta.level3.dto.PostRequestDto;
import com.sparta.level3.dto.PostResponseDto;
import com.sparta.level3.entity.Post;
import com.sparta.level3.entity.User;
import com.sparta.level3.jwt.JwtUtil;
import com.sparta.level3.repository.PostRepository;
import com.sparta.level3.repository.UserRepository;
import io.jsonwebtoken.Claims;
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

            //Entity 생성자에 Dto를 매개변수로 사용하고있는데 문제가 생길 수 있나요?
            Post post = new Post(requestDto, user.getUsername());

            post.setUser(user); // 추가함. 윗줄보다 이렇게 추가하는게 낫다.
            // user.add(post); // User에서 만든 add메서드를 이렇게 추가해도 된다!!
            postRepository.save(post);
            return new PostResponseDto(post);
        }else {
            return null;
        }
    }
    // 2. 게시글 전체 목록 조회
    @Transactional
    public List<PostResponseDto> getPostList(){
        List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> responseDto = new ArrayList<>();

//        List<PostResponseDto> tempList = postList.stream().map(post -> new PostResponseDto(post)).toList();

        for (Post post : postList) {
            responseDto.add(new PostResponseDto(post));
        }
        return responseDto;
    }

    // 3. 선택한 게시글 조회 -> 예외처리("게시글이 존재하지 않습니다")
    @Transactional
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        return new PostResponseDto(post);
    }

    // 4. 선택한 게시글 수정 -> ("아이디가 존재하지 않습니다")
    // 기본 수정 순서 : 조회 -> 수정
    // 권한이 추가된 수정 순서 : 토큰 확인 -> 토큰 검증 -> 토큰에 userId가 있는지(수정) -> 조회 -> 권한 확인 -> 수정
    @Transactional
    public PostResponseDto update(Long Id, PostRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

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
            Post post = postRepository.findById(Id).orElseThrow(
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
    // 기본 삭제 순서 : 조회 -> 삭제
    // 권한이 추가된 삭제 순서 : 토큰 확인 -> 토큰 검증 -> 토큰에 userId가 있는지 -> 조회 -> 권한 체크/post에 권한이 있는지(??) -> 삭제
    @Transactional
    public DeleteReponseDto delete(Long Id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        //토큰 확인
        if (token != null) {
            // 토큰 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            }else {
                throw new IllegalArgumentException("Token Error");
            }
            //토큰에 userId가 있는지
            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회 -> 로그인 안했으면 로그인 하라고 메시지
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("로그인 해주세요")
            );

            //조회
            Post post = postRepository.findById(Id).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")
            );

            //권한 체크? -> 현재 게시물의 게시물 작성자 이름이랑 사용자 이름이랑 같으면 가능
            if(!post.getUsername().equals(user.getUsername())){
                throw new IllegalArgumentException("해당 게시물에 권한이 존재하지 않습니다.");
            }


            //삭제
            postRepository.deleteById(Id);
            return new DeleteReponseDto("게시글 삭제 성공",200);
        }
        return null;
    }
}