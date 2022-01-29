package com.kimry.baedal.provider;

import com.kimry.baedal.CustomException;
import com.kimry.baedal.domain.User;
import com.kimry.baedal.enums.ErrorCode;
import com.kimry.baedal.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;


@Configuration
public class JwtProvider {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Autowired
    UserRepository userRepository;

    public String createJwt(String email){

        return Jwts.builder().
                setHeaderParam("alg","HS256").
                setHeaderParam("typ","JWT").
                claim("email",email).
                signWith(key).compact();
    }

    public String getHeaderToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Token");

        if (token != null && token.startsWith("Token ")) {
            token = token.substring(6);
        }
        else{
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return token;
    }

    public void validateJwt(String token){

        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        }catch(Exception e){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    public User getUser(){

        String jwt = getHeaderToken();

        Jws<Claims> jws;

        jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);

        String email = jws.getBody().get("email",String.class);

        return userRepository.findByEmail(email).get();
    }
}
