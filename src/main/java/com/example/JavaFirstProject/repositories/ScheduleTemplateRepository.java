package com.example.JavaFirstProject.repositories;

import com.example.JavaFirstProject.entitys.ScheduleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleTemplateRepository extends JpaRepository<ScheduleTemplate, String> {
}
