package com.sparta.level3.dto;

import lombok.Getter;

@Getter
public class DeleteReponseDto {
    private String msg;
    private int statusCode;

    public DeleteReponseDto(String msg, int statusCode){
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
