package com.sparta.level3.controller;

import com.sparta.level3.dto.LoginRequestDto;
import com.sparta.level3.dto.LoginResponseDto;
import com.sparta.level3.dto.SignupRequestDto;
import com.sparta.level3.dto.SignupResponseDto;
import com.sparta.level3.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return new SignupResponseDto("회원가입 성공", 200);
    }
    // 로그인
    @ResponseBody
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return new LoginResponseDto("로그인 성공", 200);
    }
}
