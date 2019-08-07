package com.management.demo.repository;


import com.management.demo.entity.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepo extends JpaRepository<ConfirmationTokenEntity,Integer> {

    ConfirmationTokenEntity findByConfirmationToken(String token);
}
