package com.kimry.baedal.controller;

import com.kimry.baedal.domain.Request;
import com.kimry.baedal.domain.User;
import com.kimry.baedal.enums.ErrorCode;
import com.kimry.baedal.enums.RequestStatus;
import com.kimry.baedal.enums.UserType;
import com.kimry.baedal.provider.JwtProvider;
import com.kimry.baedal.repository.RequestRepository;
import com.kimry.baedal.repository.UserRepository;
import com.kimry.baedal.vo.AddressVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RequestControllerTest extends BaseControllerTest{

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserRepository userRepository;

    @DisplayName("배차 요청")
    @Test
    void makeRequestTest() throws Exception{

        //given
        AddressVO addressVO = new AddressVO("서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호");
        User user = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(user);

        //when
        ResultActions resultActions = mockMvc.perform(post("/requests")
                .header("Token","Token " + jwtProvider.createJwt("customer123@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressVO)));

        //then
        resultActions.andExpect(status().isCreated());

    }

    @DisplayName("배차 요청 글자수 오류")
    @Test
    void makeRequestAddressLengthErrorTest() throws Exception{

        //given
        AddressVO addressVO = new AddressVO("서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호 123456789123456789123456789123456789123456789123456789123456789123456789");
        User user = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(user);

        //when
        ResultActions resultActions = mockMvc.perform(post("/requests")
                .header("Token","Token " + jwtProvider.createJwt("customer123@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressVO)));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(ErrorCode.ADDRESS_INVALID.getMessage()));

    }

    @DisplayName("배차 요청 드라이버 배차 요청 오류")
    @Test
    void makeRequestDriverRequestErrorTest() throws Exception{

        //given
        AddressVO addressVO = new AddressVO("서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호");
        User user = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.DRIVER, currentDateProvider.getDate());
        userRepository.save(user);

        //when
        ResultActions resultActions = mockMvc.perform(post("/requests")
                .header("Token","Token " + jwtProvider.createJwt("customer123@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressVO)));

        //then
        resultActions.andExpect(status().isForbidden())
                .andExpect(jsonPath("message").value(ErrorCode.DRIVER_CANNOT_REQUEST.getMessage()));

    }

    @DisplayName("배차 요청 이미 존재 오류")
    @Test
    void makeRequestAlreadyRequestErrorTest() throws Exception{

        //given
        AddressVO addressVO = new AddressVO("서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호");
        String address = addressVO.getAddress();

        User user = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(user);

        Request request = new Request(address, user.getId(), RequestStatus.STAND_BY, currentDateProvider.getDate());
        requestRepository.save(request);


        //when
        ResultActions resultActions = mockMvc.perform(post("/requests")
                .header("Token","Token " + jwtProvider.createJwt("customer123@gmail.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressVO)));

        //then
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("message").value(ErrorCode.ALREADY_REQUEST.getMessage()));

    }

    @DisplayName("고객 배차 요청 목록 조회")
    @Test
    void getRequestForCustomerTest() throws Exception{

        //given
        User user = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(user);

        //when
        ResultActions resultActions = mockMvc.perform(get("/requests")
                .header("Token","Token " + jwtProvider.createJwt("customer123@gmail.com")));

        //then
        resultActions.andExpect(status().isOk());

    }

    @DisplayName("드라이버 배차 요청 목록 조회")
    @Test
    void getRequestForDriverTest() throws Exception{

        //given
        User user = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.DRIVER, currentDateProvider.getDate());
        userRepository.save(user);

        //when
        ResultActions resultActions = mockMvc.perform(get("/requests")
                .header("Token","Token " + jwtProvider.createJwt("customer123@gmail.com")));

        //then
        resultActions.andExpect(status().isOk());

    }

    @DisplayName("배차 요청 승인")
    @Test
    void acceptRequestTest() throws Exception{

        //given
        String address = "서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호";

        User driver = new User("driver123@gmail.com",passwordEncoder.encode("12345"), UserType.DRIVER, currentDateProvider.getDate());
        userRepository.save(driver);

        User customer = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(customer);
        Request request = new Request(address, customer.getId(), RequestStatus.STAND_BY, currentDateProvider.getDate());
        Request result = requestRepository.save(request);


        //when
        ResultActions resultActions = mockMvc.perform(post("/requests/"+result.getId()+"/accept")
                .header("Token","Token " + jwtProvider.createJwt(driver.getEmail())));

        //then
        resultActions.andExpect(status().isOk());

    }

    @DisplayName("배차 요청 승인 고객 승인 오류")
    @Test
    void acceptRequestCustomerAcceptErrorTest() throws Exception{

        //given
        String address = "서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호";

        User driver = new User("driver123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(driver);

        User customer = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(customer);

        Request request = new Request(address, customer.getId(), RequestStatus.STAND_BY, currentDateProvider.getDate());
        Request result = requestRepository.save(request);


        //when
        ResultActions resultActions = mockMvc.perform(post("/requests/"+result.getId()+"/accept")
                .header("Token","Token " + jwtProvider.createJwt(driver.getEmail())));

        //then
        resultActions.andExpect(status().isForbidden())
                .andExpect(jsonPath("message").value(ErrorCode.CUSTOMER_CANNOT_ACCEPT.getMessage()));

    }

    @DisplayName("배차 요청 승인 요청 미존재 오류")
    @Test
    void acceptRequestNotFoundRequestErrorTest() throws Exception{

        //given
        String address = "서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호";

        User driver = new User("driver123@gmail.com",passwordEncoder.encode("12345"), UserType.DRIVER, currentDateProvider.getDate());
        userRepository.save(driver);

        User customer = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(customer);

        //when
        ResultActions resultActions = mockMvc.perform(post("/requests/1/accept")
                .header("Token","Token " + jwtProvider.createJwt(driver.getEmail())));

        //then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(ErrorCode.NOT_FOUND_REQUEST.getMessage()));

    }

    @DisplayName("배차 요청 승인 대기 상태 오류")
    @Test
    void acceptRequestNotStandByErrorTest() throws Exception{

        //given
        String address = "서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호";

        User driver = new User("driver123@gmail.com",passwordEncoder.encode("12345"), UserType.DRIVER, currentDateProvider.getDate());
        userRepository.save(driver);

        User customer = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(customer);

        Request request = new Request(address, customer.getId(), RequestStatus.ACCEPTED, currentDateProvider.getDate());
        Request result = requestRepository.save(request);


        //when
        ResultActions resultActions = mockMvc.perform(post("/requests/"+result.getId()+"/accept")
                .header("Token","Token " + jwtProvider.createJwt(driver.getEmail())));

        //then
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("message").value(ErrorCode.NOT_STANDBY.getMessage()));

    }

    @DisplayName("배차 요청 승인")
    @Test
    void completeRequestTest() throws Exception{

        //given
        String address = "서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호";

        User driver = new User("driver123@gmail.com",passwordEncoder.encode("12345"), UserType.DRIVER, currentDateProvider.getDate());
        userRepository.save(driver);

        User customer = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(customer);

        Request request = new Request(address, customer.getId(), RequestStatus.STAND_BY, currentDateProvider.getDate());
        request.accept(driver.getId(), currentDateProvider.getDate());
        Request result = requestRepository.save(request);

        //when
        ResultActions resultActions = mockMvc.perform(post("/requests/"+result.getId()+"/complete")
                .header("Token","Token " + jwtProvider.createJwt(driver.getEmail())));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("status").value(RequestStatus.COMPLETE.getStatus()));

    }

    @DisplayName("배차 요청 승인 고객 승인 오류")
    @Test
    void completeRequestCustomerRequestCompleteErrorTest() throws Exception{

        //given
        String address = "서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호";

        User driver = new User("driver123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(driver);

        User customer = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(customer);

        Request request = new Request(address, customer.getId(), RequestStatus.STAND_BY, currentDateProvider.getDate());
        request.accept(driver.getId(), currentDateProvider.getDate());
        Request result = requestRepository.save(request);

        //when
        ResultActions resultActions = mockMvc.perform(post("/requests/"+result.getId()+"/complete")
                .header("Token","Token " + jwtProvider.createJwt(driver.getEmail())));

        //then
        resultActions.andExpect(status().isForbidden())
                .andExpect(jsonPath("message").value(ErrorCode.CUSTOMER_CANNOT_COMPLETE.getMessage()));

    }

    @DisplayName("배차 요청 승인 존재 오류")
    @Test
    void completeRequestNotFoundRequestErrorTest() throws Exception{

        //given
        String address = "서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호";

        User driver = new User("driver123@gmail.com",passwordEncoder.encode("12345"), UserType.DRIVER, currentDateProvider.getDate());
        userRepository.save(driver);

        User customer = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(customer);

        Request request = new Request(address, customer.getId(), RequestStatus.STAND_BY, currentDateProvider.getDate());
        request.accept(driver.getId(), currentDateProvider.getDate());
        requestRepository.save(request);

        //when
        ResultActions resultActions = mockMvc.perform(post("/requests/0/complete")
                .header("Token","Token " + jwtProvider.createJwt(driver.getEmail())));

        //then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(ErrorCode.NOT_FOUND_REQUEST.getMessage()));

    }


    @DisplayName("배차 요청 승인 승인 상태 오류")
    @Test
    void completeRequestNotAcceptErrorTest() throws Exception{

        //given
        String address = "서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호";

        User driver = new User("driver123@gmail.com",passwordEncoder.encode("12345"), UserType.DRIVER, currentDateProvider.getDate());
        userRepository.save(driver);

        User customer = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(customer);

        Request request = new Request(address, customer.getId(), RequestStatus.STAND_BY, currentDateProvider.getDate());
        Request result = requestRepository.save(request);

        //when
        ResultActions resultActions = mockMvc.perform(post("/requests/"+result.getId()+"/complete")
                .header("Token","Token " + jwtProvider.createJwt(driver.getEmail())));

        //then
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("message").value(ErrorCode.NOT_ACCEPTED.getMessage()));

    }

    @DisplayName("배차 요청 승인 기사 불일치 오류")
    @Test
    void completeRequestNotMatchDriverErrorTest() throws Exception{

        //given
        String address = "서울특별시 짱짱구 짱짱동 123-456 짱짱빌라 789호";

        User driver = new User("driver123@gmail.com",passwordEncoder.encode("12345"), UserType.DRIVER, currentDateProvider.getDate());
        userRepository.save(driver);

        User customer = new User("customer123@gmail.com",passwordEncoder.encode("12345"), UserType.CUSTOMER, currentDateProvider.getDate());
        userRepository.save(customer);

        Request request = new Request(address, customer.getId(), RequestStatus.STAND_BY, currentDateProvider.getDate());
        request.accept(0, currentDateProvider.getDate());
        Request result = requestRepository.save(request);

        //when
        ResultActions resultActions = mockMvc.perform(post("/requests/"+result.getId()+"/complete")
                .header("Token","Token " + jwtProvider.createJwt(driver.getEmail())));

        //then
        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("message").value(ErrorCode.NOT_MATCH_DRIVER.getMessage()));

    }
}
