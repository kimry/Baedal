package com.kimry.baedal.service;

import com.kimry.baedal.CustomException;
import com.kimry.baedal.domain.User;
import com.kimry.baedal.enums.ErrorCode;
import com.kimry.baedal.enums.UserType;
import com.kimry.baedal.provider.JwtProvider;
import com.kimry.baedal.repository.UserRepository;
import com.kimry.baedal.provider.CurrentDateProvider;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    CurrentDateProvider currentDateProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public String signIn(String email, String password){

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent() && passwordEncoder.matches(password,user.get().getPassword())){
            return jwtProvider.createJwt(email);
        }

        throw new CustomException(ErrorCode.SIGN_IN_INVALID);
    }

    public User signUp(String email, String password, UserType userType){

        if(!EmailValidator.getInstance().isValid(email)){
            throw new CustomException(ErrorCode.EMAIL_INVALID);
        }

        User user = new User(email, passwordEncoder.encode(password), userType, currentDateProvider.getDate());

        if(userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED);
        }

        userRepository.save(user);

        return user;
    }
}
