package com.management.demo.service;

import com.management.demo.entity.WorkEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface WorkService {

    void add(WorkEntity userEntity) throws IOException;

    void deleteById(int id);

    Optional<WorkEntity> getById(int id);

    List<WorkEntity> getAll();


}
