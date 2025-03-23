package com.example.JavaFirstProject.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "Schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @Column(nullable = false, length = 32)
    private String id= uuid();

    @Column
    private String scheduleName;

    @Column(nullable = false)
    private Instant creation_date;

    @Column(nullable = false)
    private Instant update_date;

    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
