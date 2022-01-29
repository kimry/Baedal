package com.kimry.baedal.repository;

import com.kimry.baedal.domain.Request;
import com.kimry.baedal.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Request save(Request request);

    Optional<Request> findById(int id);

    List<Request> findAllByOrderByIdDesc();

    List<Request> findByCustomerIdOrderByIdDesc(int customerId);

    int countByCustomerIdAndStatus(int customerId, RequestStatus requestStatus);
}
