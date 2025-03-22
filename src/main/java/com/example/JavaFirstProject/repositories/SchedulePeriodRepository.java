package com.example.JavaFirstProject.repositories;

import com.example.JavaFirstProject.entitys.SchedulePeriod;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SchedulePeriodRepository extends JpaRepository<SchedulePeriod, String> {
    @Query("SELECT p FROM SchedulePeriod p " +
            "WHERE p.scheduleId = :scheduleId " +
            "AND (" +
            "   (p.executorId = :executorId)" +
            ")")
    List<SchedulePeriod> findOverlappingPeriods(
            @Param("executorId") String executorId,
            @Param("scheduleId") String scheduleId
    );
}
