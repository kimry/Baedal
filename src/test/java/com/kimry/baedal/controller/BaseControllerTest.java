package com.kimry.baedal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimry.baedal.provider.CurrentDateProvider;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    CurrentDateProvider currentDateProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

}
