package com.example.JavaFirstProject.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "Schedule_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTemplate {
    @JsonIgnore
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Id
    @Column(nullable = false, length = 32)
    private String id= uuid();

    @Column(nullable = false)
    private Instant creation_date;


    @Column(nullable = false, length = 2)
    private String template_type= generateRandomString();

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(2);
        for (int i = 0; i < 2; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
