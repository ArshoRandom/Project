package com.management.demo.repository;

import com.management.demo.entity.WorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepo extends JpaRepository<WorkEntity,Integer> {


}
