package com.sparta.post.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {
    //private Long id;
    private String password;
    private String title;
    private String author;
    private String content;
}
