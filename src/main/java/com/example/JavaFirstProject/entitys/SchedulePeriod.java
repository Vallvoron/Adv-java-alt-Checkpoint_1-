package com.example.JavaFirstProject.entitys;

import com.example.JavaFirstProject.models.type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Entity
@Table(name = "Schedule_period")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulePeriod {

    @Id
    @Column(nullable = false, length = 32)
    private String id=uuid();

    @Column(nullable = false, length = 32)
    private String slot_id;

    @Column(nullable = false, length = 32, name = "schedule_id")
    private String scheduleId;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private type slot_type = type.UNDEFINED;

    @Column(nullable = false, length = 32)
    private String administrator_id;

    @Column(length = 32)
    private String executor_id;

    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
