package com.kimry.baedal.aspect;

import com.kimry.baedal.provider.JwtProvider;
import com.kimry.baedal.repository.UserRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AuthenticationAspect {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserRepository userRepository;

    @Before("@annotation(Authentication)")
    public void authenticate() {

        String jwt = jwtProvider.getHeaderToken();
        jwtProvider.validateJwt(jwt);
    }
}
