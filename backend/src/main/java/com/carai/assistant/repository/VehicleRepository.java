package com.carai.assistant.repository;

import com.carai.assistant.entity.VehicleAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleAnalysis, Long> { }
