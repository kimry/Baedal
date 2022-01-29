package com.kimry.baedal.controller;

import com.kimry.baedal.CustomException;
import com.kimry.baedal.aspect.Authentication;
import com.kimry.baedal.domain.User;
import com.kimry.baedal.enums.ErrorCode;
import com.kimry.baedal.enums.UserType;
import com.kimry.baedal.provider.JwtProvider;
import com.kimry.baedal.vo.AddressVO;
import com.kimry.baedal.provider.CurrentDateProvider;
import com.kimry.baedal.domain.Request;
import com.kimry.baedal.enums.RequestStatus;
import com.kimry.baedal.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/requests")
public class RequestController {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    RequestService requestService;

    @Autowired
    CurrentDateProvider currentDateProvider;

    @Authentication
    @GetMapping()
    public List<Request> getRequest() {

        User user = jwtProvider.getUser();

        if(user.getUserType() == UserType.DRIVER){
            return requestService.getRequest();
        }

        return requestService.getRequest(user.getId());
    }

    @Authentication
    @PostMapping()
    public ResponseEntity<Request> makeRequest(@RequestBody AddressVO addressVO) {

        User user = jwtProvider.getUser();

        String address = addressVO.getAddress();

        if(address.length()>100 || address.length()<=0){
            throw new CustomException(ErrorCode.ADDRESS_INVALID);
        }

        if(user.getUserType()!=UserType.CUSTOMER){
            throw new CustomException(ErrorCode.DRIVER_CANNOT_REQUEST);
        }

        Request request = new Request(address, user.getId(), RequestStatus.STAND_BY, currentDateProvider.getDate());
        Request result = requestService.makeRequest(request);

        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @Authentication
    @PostMapping(value="/{id}/accept")
    public Request acceptRequest(@PathVariable("id") int id){

        User user = jwtProvider.getUser();

        if(user.getUserType()!=UserType.DRIVER){
            throw new CustomException(ErrorCode.CUSTOMER_CANNOT_ACCEPT);
        }

        return requestService.acceptRequest(id,user.getId());
    }

    @Authentication
    @PostMapping(value="/{id}/complete")
    public Request completeRequest(@PathVariable("id") int id){

        User user = jwtProvider.getUser();

        if(user.getUserType()!=UserType.DRIVER){
            throw new CustomException(ErrorCode.CUSTOMER_CANNOT_COMPLETE);
        }

        return requestService.completeRequest(id, user.getId());
    }
}
