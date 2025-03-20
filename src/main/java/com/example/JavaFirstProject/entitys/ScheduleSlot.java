package com.example.JavaFirstProject.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetTime;
import java.util.UUID;

@Entity
@Table(name = "Schedule_slot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSlot {

    @Id
    @Column(nullable = false, length = 32)
    private String id=uuid();

    @Column(nullable = false, length = 32)
    private String schedule_template_id;


    @Column(nullable = false)
    private OffsetTime begin_time;

    @Column(nullable = false)
    private OffsetTime end_time;

    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
