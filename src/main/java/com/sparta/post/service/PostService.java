package com.sparta.post.service;

import com.sparta.post.dto.PostRequestDto;
import com.sparta.post.dto.PostResponseDto;
import com.sparta.post.entity.Post;
import com.sparta.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    // 비밀번호 확인 기능 추가 필요

    //멤버 변수 선언
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto){
        //RequestDto -> Entity
        Post post = new Post(requestDto);

        //DB 저장
        Post savePost = postRepository.save(post);

        //Entity -> ResponseDto
        return new PostResponseDto(savePost);
    }

    public List<PostResponseDto> getPosts(){
        //DB 조회
        return postRepository.findAll().stream().map(PostResponseDto::new).toList();
    }

    public List<PostResponseDto> getPost(Long id){
        // id 로 조회
        return postRepository.findById(id).stream().map(PostResponseDto::new).toList();
    }

    @Transactional //변경 감지
    public List<PostResponseDto> updatePost(Long id, String password, PostRequestDto requestDto){
        // 해당 post DB에 존재하는지 확인
        Post post = findPost(id);

        // 비밀번호 확인
        if(!password.equals(requestDto.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        // post 내용 수정
        post.update(requestDto);

        return postRepository.findById(id).stream().map(PostResponseDto::new).toList();
    }

    // deleted 메서드에 @Transactional 적용되어 있음
    public String deletePost(Long id, String password, PostRequestDto requestDto){
        // 해당 post DB에 존재하는지 확인
        Post post = findPost(id);

        // 비밀번호 확인
        if(!password.equals(requestDto.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        //post 삭제
        postRepository.delete(post);

        return "{\"success\":\"true\"}";
    }

    private Post findPost(Long id){
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")
        );
    }
}
