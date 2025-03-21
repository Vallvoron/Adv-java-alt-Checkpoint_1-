package com.example.JavaFirstProject.controllers;


import com.example.JavaFirstProject.entitys.*;
import com.example.JavaFirstProject.models.*;
import com.example.JavaFirstProject.repositories.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedule")
@Tag(name = "Сервис управления расписаниями")
@Validated
public class ScheduleController {
    private final ScheduleRepository ScheduleDb;
    private final SchedulePeriodRepository SchedulePeriodDb;
    private final ScheduleTemplateRepository ScheduleTemplateDb;
    private final ScheduleSlotRepository ScheduleSlotDb;
    private final EmployeeRepository EmployeeDb;

    public ScheduleController(ScheduleRepository ScheduleDb, SchedulePeriodRepository SchedulePeriodDb,
                              ScheduleTemplateRepository ScheduleTemplateDb, ScheduleSlotRepository ScheduleSlotDb, EmployeeRepository EmployeeDb){
        this.ScheduleDb = ScheduleDb;
        this.SchedulePeriodDb = SchedulePeriodDb;
        this.ScheduleTemplateDb = ScheduleTemplateDb;
        this.ScheduleSlotDb = ScheduleSlotDb;
        this.EmployeeDb = EmployeeDb;
    }

    @PostMapping("/createSchedule")
    @Operation(
            summary = "Создание расписания"
    )
    public ResponseEntity<?> createSchedule(@Valid @RequestBody CreateSchedule request){
        try {
            Schedule newschedule = new Schedule();
            newschedule.setSchedule_name(request.getSchedule_name());
            newschedule.setUpdate_date(Instant.now());
            newschedule.setCreation_date(Instant.now());

            ScheduleDb.save(newschedule);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Расписание успешно создано");
        }catch (Exception error) {
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    };

    @GetMapping("/getSchedule")
    @Operation(
            summary = "Получение расписания"
    )
    public ResponseEntity<?> getSchedule(@Valid @RequestParam String scheduleId){
        try {
            Optional<Schedule> schedule = ScheduleDb.findById(scheduleId);
            if (schedule.isEmpty()){
                ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body("Расписания с указанным id не существует");
            }
            Schedule scheduleRes = schedule.get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(scheduleRes);
        }catch (Exception error) {
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    };

    @PostMapping("/createScheduleTemplate")
    @Operation(
            summary = "Создание шаблона расписания"
    )
    public ResponseEntity<?> createScheduleTemplate(){
        try {
            ScheduleTemplate newScheduleTemplate = new ScheduleTemplate();
            newScheduleTemplate.setCreation_date(Instant.now());

            ScheduleTemplateDb.save(newScheduleTemplate);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Шаблон Расписания успешно создан");
        }catch (Exception error) {
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    };

    @GetMapping("/getScheduleTemplate")
    @Operation(
            summary = "Получение шаблона расписания"
    )
    public ResponseEntity<?> getScheduleTemplate(@Valid @RequestParam String templateId){
        try {
            Optional<ScheduleTemplate> scheduleTemplate = ScheduleTemplateDb.findById(templateId);
            if (scheduleTemplate.isEmpty()){
                ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body("Шаблона с указанным id не существует");
            }
            ScheduleTemplate scheduleTemplateRes = scheduleTemplate.get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(scheduleTemplateRes);
        }catch (Exception error) {
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    };


    @PostMapping("/createScheduleSlot")
    @Operation(
            summary = "Создание слота расписания"
    )
    public ResponseEntity<?> createScheduleSlot(@Valid @RequestBody CreateSlot request){
        try {
            if (request.getBegin_time().isAfter(request.getEnd_time())){
                ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body("Дата начала не должна быть после даты окончания");
            }
            Optional<ScheduleTemplate> scheduleTemplate = ScheduleTemplateDb.findById(request.getSchedule_template_id());
            if (scheduleTemplate.isEmpty()){
                ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body("Шаблона с указанным id не существует");
            }

            ScheduleSlot newScheduleSlot = new ScheduleSlot();
            newScheduleSlot.setSchedule_template_id(request.getSchedule_template_id());
            newScheduleSlot.setBegin_time(request.getBegin_time());
            newScheduleSlot.setEnd_time(request.getEnd_time());

            ScheduleSlotDb.save(newScheduleSlot);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Слот Расписания успешно создан");
        }catch (Exception error) {
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    };

    @GetMapping("/getScheduleSlot")
    @Operation(
            summary = "Получение слота расписания"
    )
    public ResponseEntity<?> getScheduleSlot(@Valid @RequestParam String slotId){
        try {
            Optional<ScheduleSlot> scheduleSlot = ScheduleSlotDb.findById(slotId);
            if (scheduleSlot.isEmpty()){
                ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body("Слота с указанным id не существует");
            }
            ScheduleSlot scheduleSlotRes = scheduleSlot.get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(scheduleSlotRes);
        }catch (Exception error) {
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    };


    @PostMapping("/createEmployee")
    @Operation(
            summary = "Создание сотрудника"
    )
    public ResponseEntity<?> createEmployee(@Valid @RequestBody CreateEmployee request){
        try {
            Employee newEmployee = new Employee();
            newEmployee.setEmployee_name(request.getEmployee_name());
            newEmployee.setStatus(request.getStatus());
            newEmployee.setPosition(request.getPosition());

            EmployeeDb.save(newEmployee);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Сотрудник успешно создан");
        }catch (Exception error) {
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    };

    @GetMapping("/getEmployee")
    @Operation(
            summary = "Получение сотрудника"
    )
    public ResponseEntity<?> getEmployee(@Valid @RequestParam String employeeId){
        try {
            Optional<Employee> employee = EmployeeDb.findById(employeeId);
            if (employee.isEmpty()){
                ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body("Сотрудника с указанным id не существует");
            }
            Employee employeeRes = employee.get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(employeeRes);
        }catch (Exception error) {
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    };
}
