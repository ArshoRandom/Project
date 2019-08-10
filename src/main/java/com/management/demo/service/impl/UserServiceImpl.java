package com.management.demo.service.impl;

import com.management.demo.entity.Role;
import com.management.demo.entity.UserEntity;
import com.management.demo.repository.RoleRepo;
import com.management.demo.repository.UserRepo;
import com.management.demo.repository.redis.UserRedis;
import com.management.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private UserRedis userRedis;
    private RoleRepo roleRepo;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, UserRedis userRedis, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userRedis = userRedis;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void add(UserEntity user, String role, MultipartFile file) throws IOException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepo.findByRole(role);
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        String filePath = new File("src/main/resources/images").getAbsolutePath();
        byte[] bytes = file.getBytes();
        Path path = Paths.get(filePath + File.separator + file.getOriginalFilename());
        Files.write(path,bytes);
        user.setImageUrl(filePath+file.getOriginalFilename()+user.getEmail());
        userRepo.save(user);
        userRedis.addInRedis(String.valueOf(user.getId()),user);
    }

    @Override
    public Optional<UserEntity> getByEmail(String email) {
        return  userRepo.findByEmail(email);
    }

    @Override
       public void deleteById(int id) {
        this.userRedis.deleteFromRedis(String.valueOf(id));
        this.userRepo.deleteById(id);
    }

    @Override
    public List<UserEntity> findAllByUserRole(Role role) {
      return this.userRepo.findAllByUserRole(role);
    }

    @Override
    public Optional<UserEntity> getById(int id) {
        Optional<UserEntity> userEntity = this.userRedis.fetchFromRedis(String.valueOf(id));
        if (!userEntity.isPresent()){
            userEntity = this.userRepo.findById(id);
            if (userEntity.isPresent()){
                this.userRedis.addInRedis(String.valueOf(id),userEntity.get());
            }
        }
        return userEntity;
    }

    @Override
    public List<UserEntity> getAll() {
        return this.userRepo.findAll();
    }

    @Override
    public void setActivated(boolean isActivated, String email) {
        this.userRepo.setIsActivated(isActivated,email);
    }

    @Override
    public Optional<UserEntity> getByEmailAndPassword(String email, String password) {
        return this.userRepo.findByEmailAndPassword(email,password);
    }
}
