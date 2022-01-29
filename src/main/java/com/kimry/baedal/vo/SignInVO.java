package com.kimry.baedal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SignInVO {

    private String email;

    private String password;
}
