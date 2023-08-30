package com.sparta.post.service;

import com.sparta.post.dto.LoginRequestDto;
import com.sparta.post.dto.SignupRequestDto;
import com.sparta.post.entity.Message;
import com.sparta.post.entity.User;
import com.sparta.post.jwt.JwtUtil;
import com.sparta.post.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<Message> signup(SignupRequestDto requestDto) {

        Message msg = new Message();
        msg.setStatus(200);
        msg.setMsg("회원가입 성공");

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // username 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if(checkUsername.isPresent()){
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 등록
        User user = new User(username, password);
        userRepository.save(user);

        return new ResponseEntity<>(msg, null, HttpStatus.OK);
    }

    public ResponseEntity<Message> login(LoginRequestDto requestDto, HttpServletResponse res) {

        Message msg = new Message();
        msg.setStatus(200);
        msg.setMsg("로그인 성공");

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인 check 필요
        if(!password.equals(user.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        // 인증이 완료후 JWT 생성및 쿠키에 저장후 Response 객체에 추가
        // 구현후 객체 주입 필요
        String token = jwtUtil.createToken(user.getUsername());
        jwtUtil.addJwtToCookie(token, res);

        return new ResponseEntity<>(msg, null, HttpStatus.OK);

    }

//    public String signup(@Valid SignupRequestDto requestDto) {
//        String username = requestDto.getUsername();
//        String password = requestDto.getPassword();
//
//        // username 중복 확인
//        Optional<User> checkUsername = userRepository.findByUsername(username);
//        if(checkUsername.isPresent()){
//            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
//        }
//
//        // 사용자 등록
//        User user = new User(username, password);
//        userRepository.save(user);
//
//        return "{\"msg\":\"회원가입 성공\",\"statusCode\":200}";
//    }
//
//    public String login(LoginRequestDto requestDto, HttpServletResponse res) {
//        String username = requestDto.getUsername();
//        String password = requestDto.getPassword();
//
//        // 사용자 확인
//        User user = userRepository.findByUsername(username).orElseThrow(
//                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
//        );
//
//        // 비밀번호 확인 check 필요
//        if(!password.equals(user.getPassword()))
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//
//        // 인증이 완료후 JWT 생성및 쿠키에 저장후 Response 객체에 추가
//        // 구현후 객체 주입 필요
//        String token = jwtUtil.createToken(user.getUsername());
//        jwtUtil.addJwtToCookie(token, res);
//
//        return "{\"msg\":\"로그인 성공\",\"statusCode\":200}";
//
//    }
}
