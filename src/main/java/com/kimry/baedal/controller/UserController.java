package com.kimry.baedal.controller;

import com.kimry.baedal.CustomException;
import com.kimry.baedal.enums.ErrorCode;
import com.kimry.baedal.enums.UserType;
import com.kimry.baedal.vo.SignInVO;
import com.kimry.baedal.vo.SignUpVO;
import com.kimry.baedal.domain.User;
import com.kimry.baedal.provider.CurrentDateProvider;
import com.kimry.baedal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/users")
public class UserController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    CurrentDateProvider currentDateProvider;

    @PostMapping(value="/sign-in")
    public ResponseEntity<Map<String, String>> signIn(@RequestBody SignInVO signInVO){

        String email = signInVO.getEmail();
        String password = signInVO.getPassword();

        String token = userService.signIn(email, password);

        Map<String, String> message = new HashMap<>();
        message.put("accessToken",token);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping(value="/sign-up")
    public ResponseEntity<User> signUp(@RequestBody SignUpVO signUpVO){

        String email = signUpVO.getEmail();
        String password = signUpVO.getPassword();
        String userTypeStr = signUpVO.getUserType();

        UserType userType = UserType.value(userTypeStr);

        if(userType==null){
            throw new CustomException(ErrorCode.USER_TYPE_INVALID);
        }

        User result = userService.signUp(email, password, userType);

        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }
}
