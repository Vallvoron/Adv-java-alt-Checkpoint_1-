package com.example.JavaFirstProject.repositories;

import com.example.JavaFirstProject.entitys.SchedulePeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulePeriodRepository extends JpaRepository<SchedulePeriod, String> {
}
