package com.example.JavaFirstProject.repositories;

import com.example.JavaFirstProject.entitys.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
}
