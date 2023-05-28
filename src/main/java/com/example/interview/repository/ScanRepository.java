package com.example.interview.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.example.interview.entity.Scan;

public interface ScanRepository extends CrudRepository<Scan, Integer> {

    List<Scan> findByTypeAndTimestampBetween(String type, LocalDateTime startDate, LocalDateTime endDate);
}