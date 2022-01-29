package com.kimry.baedal.domain;

import com.kimry.baedal.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String address;

    @Nullable
    private Integer driverId;

    private int customerId;

    private RequestStatus status;

    @Nullable
    private String completedAt;

    @Nullable
    private String acceptedAt;

    private String createdAt;

    private String updatedAt;

    public Request(String address, int customerId, RequestStatus status, String currentDate) {
        this.address = address;
        this.customerId = customerId;
        this.status = status;
        this.createdAt = currentDate;
        this.updatedAt = currentDate;
    }

    public void accept(int driverId, String currentDate){
        this.driverId = driverId;
        this.status = RequestStatus.ACCEPTED;
        this.acceptedAt = currentDate;
        this.updatedAt = currentDate;
    }

    public void complete(String currentDate){
        this.status = RequestStatus.COMPLETE;
        this.completedAt = currentDate;
        this.updatedAt = currentDate;
    }
}
