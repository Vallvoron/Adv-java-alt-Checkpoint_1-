package com.example.JavaFirstProject.repositories;

import com.example.JavaFirstProject.entitys.SchedulePeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchedulePeriodRepository extends JpaRepository<SchedulePeriod, String> {
    Optional<List<SchedulePeriod>> findAllByScheduleId(String Schedule_Id);
}
