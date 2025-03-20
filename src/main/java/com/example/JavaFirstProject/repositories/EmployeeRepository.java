package com.example.JavaFirstProject.repositories;

import com.example.JavaFirstProject.entitys.Employee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeRepository extends JpaRepository<Employee, String> {
}
