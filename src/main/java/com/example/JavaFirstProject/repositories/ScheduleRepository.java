package com.example.JavaFirstProject.repositories;

import com.example.JavaFirstProject.entitys.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    Optional<Schedule> findByScheduleName(String name);
}
