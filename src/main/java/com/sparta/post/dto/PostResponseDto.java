package com.sparta.post.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sparta.post.entity.Comment;
import com.sparta.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
  //  private String comments;
    private List<ForResponseComment> comments = new ArrayList<>();
//    private List<CommentResponseDto> commentResponseDto = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        StringBuilder sb = new StringBuilder();
       // for(Comment cm : post.getComments()){
      //      sb.append(cm.toString());
     //   }
      //  this.comments = sb.toString(); //new ArrayList<Comment>(post.getComments());
       // System.out.println(comments);
        for(Comment comment : post.getComments()){
            ForResponseComment cm = new ForResponseComment(comment);
            comments.add(cm);
        }
    }
}
