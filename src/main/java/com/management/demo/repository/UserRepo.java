package com.management.demo.repository;


import com.management.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndPassword(String email, String password);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users u SET u.is_activated = :is_activated where u.email = :email",nativeQuery = true)
    void setIsActivated(@Param("is_activated") boolean isActivated,
                        @Param("email") String email);

}
