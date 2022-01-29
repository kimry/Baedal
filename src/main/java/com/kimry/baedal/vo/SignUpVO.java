package com.kimry.baedal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SignUpVO {

    private String email;

    private String password;

    private String userType;
}
