package com.sparta.post.entity;

import com.sparta.post.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "post") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "author", nullable = false)
    private String author;
    @Column(name = "content", nullable = false)
    private String content;

    public Post(PostRequestDto requestDto) {
        this.password = requestDto.getPassword();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.author = requestDto.getAuthor();
    }


    public void update(PostRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.author = requestDto.getAuthor();
        this.content = requestDto.getContent();
    }
}
