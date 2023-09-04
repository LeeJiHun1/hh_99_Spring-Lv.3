package com.sparta.post.dto;

import com.sparta.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostCommentResponseDto {

    private Long id;

    private String title;
    private String username;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<CommentResponseDto> commentResponseDto;

    public PostCommentResponseDto(Post post, List<CommentResponseDto> commentResponseDto) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();

        this.commentResponseDto = commentResponseDto;
    }
}
