package com.sparta.post.service;

import com.sparta.post.dto.PostRequestDto;
import com.sparta.post.dto.PostResponseDto;
import com.sparta.post.entity.Message;
import com.sparta.post.entity.Post;
import com.sparta.post.jwt.JwtUtil;
import com.sparta.post.repository.PostRepository;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    //멤버 변수 선언
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    public PostService(PostRepository postRepository, JwtUtil jwtUtil) {
        this.postRepository = postRepository;
        this.jwtUtil = jwtUtil;
    }

    public PostResponseDto createPost(PostRequestDto requestDto, String tokenValue){

        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if(!jwtUtil.validateToken(token)){
            throw new IllegalArgumentException("Token error");
        }

        //username 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        String username = info.getSubject();

        //RequestDto -> Entity
        Post post = new Post(requestDto,username);

        //DB 저장
        Post savePost = postRepository.save(post);

        //Entity -> ResponseDto
        return new PostResponseDto(savePost);
    }

    public List<PostResponseDto> getPosts(){
        //DB 조회
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public List<PostResponseDto> getPost(Long id){
        // id 로 조회
        return postRepository.findById(id).stream().map(PostResponseDto::new).toList();
    }

    @Transactional //변경 감지(Dirty Checking), 부모메서드인 updatePost
    public List<PostResponseDto> updatePost(Long id, PostRequestDto requestDto, String tokenValue){

        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if(!jwtUtil.validateToken(token)){
            throw new IllegalArgumentException("Token error");
        }

        // 해당 post DB에 존재하는지 확인 수정필요
        Post post = findPost(id);

        // 해당 사용자(username)가 작성한 게시글인지 확인
        // setSubject(username)
        Claims info = jwtUtil.getUserInfoFromToken(token);
        String username = info.getSubject();

        if(!username.equals(post.getUsername())){
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }

        // post 내용 수정
        post.update(requestDto);

        return postRepository.findById(id).stream().map(PostResponseDto::new).toList();
    }

    public ResponseEntity<Message> deletePost(Long id, String tokenValue){

        Message msg = new Message();
        msg.setStatus(200);
        msg.setMsg("게시글 삭제 성공");

        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if(!jwtUtil.validateToken(token)){
            throw new IllegalArgumentException("Token error");
        }

        // 해당 post DB에 존재하는지 확인
        Post post = findPost(id);

        // 해당 사용자(username)가 작성한 게시글인지 확인
        // setSubject(username)
        Claims info = jwtUtil.getUserInfoFromToken(token);
        String username = info.getSubject();
        if(!username.equals(post.getUsername())){
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }

        //post 삭제
        postRepository.delete(post);

        return new ResponseEntity<>(msg, null, HttpStatus.OK);
    }

    // deleted 메서드에 @Transactional 적용되어 있음
//    public String deletePost(Long id, String tokenValue){
//
//        // JWT 토큰 substring
//        String token = jwtUtil.substringToken(tokenValue);
//
//        // 토큰 검증
//        if(!jwtUtil.validateToken(token)){
//            throw new IllegalArgumentException("Token error");
//        }
//
//        // 해당 post DB에 존재하는지 확인
//        Post post = findPost(id);
//
//        // 해당 사용자(username)가 작성한 게시글인지 확인
//        // setSubject(username)
//        Claims info = jwtUtil.getUserInfoFromToken(token);
//        String username = info.getSubject();
//        if(!username.equals(post.getUsername())){
//            throw new IllegalArgumentException("사용자 정보가 없습니다.");
//        }
//
//        //post 삭제
//        postRepository.delete(post);
//
//        return "{\"msg\":\"게시글 삭제 성공\",\"statusCode\":200}";
//    }

    private Post findPost(Long id){
        //findById -> Optional type -> Null Check
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")
        );
    }

}
