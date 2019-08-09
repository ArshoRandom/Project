package com.management.demo.email;


import com.management.demo.email.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepo extends JpaRepository<ConfirmationTokenEntity,Integer> {

    ConfirmationTokenEntity findByConfirmationToken(String token);
}
