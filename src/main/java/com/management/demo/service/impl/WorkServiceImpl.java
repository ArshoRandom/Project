package com.management.demo.service.impl;

import com.management.demo.entity.WorkEntity;
import com.management.demo.repository.WorkRepo;
import com.management.demo.repository.redis.UserRedis;
import com.management.demo.repository.redis.WorkRedis;
import com.management.demo.service.WorkService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service("workService")
public class WorkServiceImpl implements WorkService {

    private WorkRepo workRepo;
    private WorkRedis workRedis;

    public WorkServiceImpl(WorkRepo workRepo, WorkRedis workRedis) {
        this.workRepo = workRepo;
        this.workRedis = workRedis;
    }


    @Override
    public void add(WorkEntity workEntity) throws IOException {
        if(workEntity != null){
            workRepo.save(workEntity);
            workRedis.addInRedis(String.valueOf(workEntity.getId()),workEntity);
        }
    }

    @Override
    public void deleteById(int id) {
        this.workRepo.deleteById(id);
    }

    @Override
    public Optional<WorkEntity> getById(int id) {
        Optional<WorkEntity> workEntity = this.workRedis.fetchFromRedis(String.valueOf(id));
        if (!workEntity.isPresent()){
            workEntity = this.workRepo.findById(id);
            if (workEntity.isPresent()){
                this.workRedis.addInRedis(String.valueOf(id),workEntity.get());
            }
        }
        return workEntity;
    }

    @Override
    public List<WorkEntity> getAll() {
        return this.workRepo.findAll();
    }

}
