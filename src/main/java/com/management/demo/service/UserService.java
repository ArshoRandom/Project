package com.management.demo.service;

import com.management.demo.entity.Role;
import com.management.demo.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    void add(UserEntity userEntity, String role, MultipartFile file) throws IOException;

    Optional<UserEntity> getByEmail(String email);

    void deleteById(int id);

    Optional<UserEntity> getById(int id);

    List<UserEntity> getAll();

    void setActivated(boolean isActivated, String email);

    Optional<UserEntity> getByEmailAndPassword(String email, String password);

    List<UserEntity> findAllByUserRole(Role role);

}
