package com.example.JavaFirstProject.entitys;

import com.example.JavaFirstProject.models.emp_position;
import com.example.JavaFirstProject.models.emp_status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @Column(nullable = false, length = 32)
    private String id=uuid();

    @Column(nullable = false)
    private String employee_name;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private emp_status status;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private emp_position position = emp_position.UNDEFINED;

    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
