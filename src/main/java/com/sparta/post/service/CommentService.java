package com.sparta.post.service;

import com.sparta.post.dto.CommentRequestDto;
import com.sparta.post.dto.CommentResponseDto;
import com.sparta.post.entity.Comment;
import com.sparta.post.entity.Message;
import com.sparta.post.jwt.JwtUtil;
import com.sparta.post.repository.CommentRepository;
import com.sparta.post.repository.PostRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    public ResponseEntity<?> createComment(CommentRequestDto requestDto, String tokenValue) {
        String token = jwtUtil.substringToken(tokenValue);

        if(!jwtUtil.validateToken(token)){
            Message msg = new Message(400, "토큰이 유효하지 않습니다.");
            return new ResponseEntity<>(msg, null, HttpStatus.BAD_REQUEST);
        }

        // DB에 해당 PostId 확인
        findPost(requestDto.getPostId());

        // username 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        String username = info.getSubject();

        //RequestDto -> Entity
        Comment comment = new Comment(requestDto, username);

        //DB 저장
        Comment saveComment = commentRepository.save(comment);

        //Entity -> ResponseDto
        return new ResponseEntity<>(new CommentResponseDto(saveComment),null, HttpStatus.OK );
    }

    @Transactional
    public ResponseEntity<?> updateComment(Long id, CommentRequestDto requestDto, String tokenValue) {

        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if(!jwtUtil.validateToken(token)){
            Message msg = new Message(400, "토큰이 유효하지 않습니다.");
            return new ResponseEntity<>(msg, null, HttpStatus.BAD_REQUEST);
        }

        // 해당 유저의 댓굴 id 값이 DB 에 존재하는지 확인
        Optional<Comment> checkComment = commentRepository.findById(id);
        Comment comment;
        if (checkComment.isPresent()) {
            comment = checkComment.get();
        } else {
            return new ResponseEntity<>(new Message(400, "작성자만 수정할 수 있습니다."), null, HttpStatus.BAD_REQUEST);
        }

         // comment 수정
        comment.update(requestDto);

        return new ResponseEntity<>(new CommentResponseDto(comment),null, HttpStatus.OK );
    }

    public ResponseEntity<?> deleteComment(Long id, String tokenValue) {

        Message msg = new Message(200, "댓글 삭제 성공");

        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            msg = new Message(400, "토큰이 유효하지 않습니다.");
            return new ResponseEntity<>(msg, null, HttpStatus.BAD_REQUEST);
        }

        // 해당 유저의 댓굴 id 값이 DB 에 존재하는지 확인
        Optional<Comment> checkComment = commentRepository.findById(id);
        Comment comment;
        if (checkComment.isPresent()) {
            comment = checkComment.get();
        } else {
            return new ResponseEntity<>(new Message(400, "작성자만 삭제할 수 있습니다."), null, HttpStatus.BAD_REQUEST);
        }

        // comment 삭제
        commentRepository.delete(comment);

        return new ResponseEntity<>(msg, null, HttpStatus.OK);
    }

    private void findPost(Long id){
        //findById -> Optional type -> Null Check
        postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }
}
