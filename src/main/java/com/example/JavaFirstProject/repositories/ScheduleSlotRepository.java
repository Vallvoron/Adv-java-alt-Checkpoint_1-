package com.example.JavaFirstProject.repositories;

import com.example.JavaFirstProject.entitys.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, String> {
}
