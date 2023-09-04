package com.sparta.post.entity;

import com.sparta.post.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "username", nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(CommentRequestDto requestDto, String username) {
        this.content = requestDto.getContent();
        this.username = username;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    @Override
    public String toString(){
        return "id : "+this.id+",\ncontent : "+this.content+" ,\n createdAt : "+this.getCreatedAt()+",\n modifedAt : "+ this.getModifiedAt()+",\n username : "+this.username;
    }
}
