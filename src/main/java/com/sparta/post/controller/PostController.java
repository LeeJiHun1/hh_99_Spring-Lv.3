package com.sparta.post.controller;

import com.sparta.post.dto.PostRequestDto;
import com.sparta.post.dto.PostResponseDto;
import com.sparta.post.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// Json 형태로 객체 데이터를 반환
// @ResponseBody + @Controller
// Model 객체를 만들어 데이터를 담고 view 찾기
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // @RequestBody는 Json 형식으로 넘겨주어야한다.
    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto){
        return postService.createPost(requestDto);
    }

    @GetMapping("/posts")
    public List<PostResponseDto> getPosts(){
        return postService.getPosts();
    }

    // @RequestBody -> Json 기반의 메시지를 사용하는 요청의 경우
    @GetMapping("/post/{id}")
    public List<PostResponseDto> getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    //@PathVariable uri -> id
    @PutMapping("/post/{id}")
    public List<PostResponseDto> updatePost(@PathVariable Long id,@RequestBody PostRequestDto requestDto){
        return postService.updatePost(id, requestDto);
    }

    @DeleteMapping("/post/{id}")
    public String deletePost(@PathVariable Long id,@RequestBody PostRequestDto requestDto){
        return postService.deletePost(id, requestDto);
    }

}
