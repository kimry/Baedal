package com.kimry.baedal.service;

import com.kimry.baedal.CustomException;
import com.kimry.baedal.enums.ErrorCode;
import com.kimry.baedal.enums.RequestStatus;
import com.kimry.baedal.provider.CurrentDateProvider;
import com.kimry.baedal.domain.Request;
import com.kimry.baedal.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    CurrentDateProvider currentDateProvider;

    @Autowired
    RequestRepository requestRepository;

    public List<Request> getRequest() {
        return requestRepository.findAllByOrderByIdDesc();
    }

    public List<Request> getRequest(int customerId) {

        return requestRepository.findByCustomerIdOrderByIdDesc(customerId);

    }

    public Request makeRequest(Request request){

        if(requestRepository.countByCustomerIdAndStatus(request.getCustomerId(), request.getStatus())>=1) {

            throw new CustomException(ErrorCode.ALREADY_REQUEST);
        }

        return requestRepository.save(request);
    }

    public Request acceptRequest(int id, int driverId){

        Optional<Request> request = requestRepository.findById(id);

        if(!request.isPresent()) {
            throw new CustomException(ErrorCode.NOT_FOUND_REQUEST);
        }

        if(request.get().getStatus() != RequestStatus.STAND_BY) {
            throw new CustomException(ErrorCode.NOT_STANDBY);
        }

        request.get().accept(driverId, currentDateProvider.getDate());

        return requestRepository.save(request.get());
    }

    public Request completeRequest(int id, int driverId){

        Optional<Request> request = requestRepository.findById(id);

        if(!request.isPresent()) {
            throw new CustomException(ErrorCode.NOT_FOUND_REQUEST);
        }

        if(request.get().getStatus() != RequestStatus.ACCEPTED) {
            throw new CustomException(ErrorCode.NOT_ACCEPTED);
        }

        if(request.get().getDriverId()!= driverId){
            throw new CustomException(ErrorCode.NOT_MATCH_DRIVER);
        }

        request.get().complete(currentDateProvider.getDate());

        return requestRepository.save(request.get());
    }
}
