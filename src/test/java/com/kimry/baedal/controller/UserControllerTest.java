package com.kimry.baedal.controller;

import com.kimry.baedal.domain.User;
import com.kimry.baedal.enums.ErrorCode;
import com.kimry.baedal.enums.UserType;
import com.kimry.baedal.repository.UserRepository;
import com.kimry.baedal.vo.SignInVO;
import com.kimry.baedal.vo.SignUpVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseControllerTest{

    @Autowired
    UserRepository userRepository;

    @DisplayName("회원가입 성공")
    @Test
    void signUpTest() throws Exception{

        //given
        SignUpVO signUpVO =  new SignUpVO("nalda1538@gmail.com","12345","customer");

        //when
        ResultActions resultActions = mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpVO)));

        //then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("email").value("nalda1538@gmail.com"))
                .andExpect(jsonPath("userType").value("customer"));
    }

    @DisplayName("회원가입 이메일 유효성 검사 오류")
    @Test
    void signUpEmailInvalidErrorTest() throws Exception{

        //given
        SignUpVO signUpVO =  new SignUpVO("nalda1538@gmail.czzz","12345","customer");

        //when
        ResultActions resultActions = mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpVO)));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(ErrorCode.EMAIL_INVALID.getMessage()));
    }

    @DisplayName("회원가입 유저타입 검사 오류")
    @Test
    void signUpUserTypeInvalidErrorTest() throws Exception{

        //given
        SignUpVO signUpVO =  new SignUpVO("nalda1538@gmail.com","12345","custome");

        //when
        ResultActions resultActions = mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpVO)));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(ErrorCode.USER_TYPE_INVALID.getMessage()));
    }
    
    @DisplayName("회원가입 이미 가입된 이메일 오류")
    @Test
    void signUpAlreadyRegisteredErrorTest() throws Exception{

        //given
        SignUpVO signUpVO = new SignUpVO("nalda1538@gmail.com","12345","customer");
        User user = new User("nalda1538@gmail.com",passwordEncoder.encode("12345"),UserType.CUSTOMER,currentDateProvider.getDate());
        userRepository.save(user);

        //when
        ResultActions resultActions = mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpVO)));

        //then
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("message").value(ErrorCode.ALREADY_REGISTERED.getMessage()));
    }

    @DisplayName("로그인 성공")
    @Test
    void signInTest() throws Exception{

        //given
        SignInVO signInVO =  new SignInVO("nalda1538@gmail.com","12345");
        User user = new User("nalda1538@gmail.com",passwordEncoder.encode("12345"),UserType.CUSTOMER,currentDateProvider.getDate());
        userRepository.save(user);

        //when
        ResultActions resultActions = mockMvc.perform(post("/users/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInVO)));

        //then
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("로그인 비밀번호 검증 오류")
    @Test
    void signInPasswordErrorTest() throws Exception{

        //given
        SignInVO signInVO =  new SignInVO("nalda1538@gmail.com","12345");
        User user = new User("nalda1538@gmail.com",passwordEncoder.encode("123456"),UserType.CUSTOMER,currentDateProvider.getDate());
        userRepository.save(user);

        //when
        ResultActions resultActions = mockMvc.perform(post("/users/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInVO)));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(ErrorCode.SIGN_IN_INVALID.getMessage()));
    }

    @DisplayName("로그인 이메일 검증 오류")
    @Test
    void signInNoEmailErrorTest() throws Exception{

        //given
        SignInVO signInVO =  new SignInVO("nalda1538@gmail.com","12345");

        //when
        ResultActions resultActions = mockMvc.perform(post("/users/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInVO)));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(ErrorCode.SIGN_IN_INVALID.getMessage()));
    }

}
