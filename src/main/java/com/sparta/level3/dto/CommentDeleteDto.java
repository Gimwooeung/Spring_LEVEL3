package com.sparta.level3.dto;

import lombok.Getter;

@Getter
public class CommentDeleteDto {
    private String response;

    public CommentDeleteDto(String response) {
        this.response = response;
    }
}