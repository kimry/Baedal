package com.kimry.baedal.repository;

import com.kimry.baedal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    Optional<User> findByEmail(String email);

}
