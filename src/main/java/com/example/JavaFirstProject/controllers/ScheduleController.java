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
import java.time.OffsetTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public ResponseEntity<?> createSchedule(@Valid @RequestParam CreateSchedule request){
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
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Расписания с указанным id не существует");
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
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Шаблона с указанным id не существует");
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
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Дата начала не должна быть после даты окончания");
            }
            Optional<ScheduleTemplate> scheduleTemplate = ScheduleTemplateDb.findById(request.getSchedule_template_id());

            if (scheduleTemplate.isEmpty()){
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Шаблона с указанным id не существует");
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
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Слота с указанным id не существует");
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
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Сотрудника с указанным id не существует");
            }
            Employee employeeRes = employee.get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(employeeRes);
        }catch (Exception error) {
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    };

    @PostMapping("/createPeriod")
    @Operation(
            summary = "Создание периода расписания"
    )
    public ResponseEntity<?> createPeriod(@RequestHeader("x-current-user") String administratorId, @Valid @RequestBody CreatePeriod request){
        try {
            Optional<Employee> user = EmployeeDb.findById(administratorId);
            if (user.isEmpty()) {
                return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Пользователь не наден"));
            }

            Employee administrator = user.get();

            if (!Objects.equals(administrator.getPosition().toString(), "MANAGER")) {
                return ResponseEntity.status(403).contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Запрос отклонен, недостаточно прав"));
            }
            SchedulePeriod newPeriod = new SchedulePeriod();
            Optional<Schedule> scheduleOptional = ScheduleDb.findById(request.getSchedule_id());
            if (scheduleOptional.isEmpty()) {
                return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Расписание не найдено"));
            }
            Schedule schedule = scheduleOptional.get();
            Optional<ScheduleSlot> scheduleSlotOptional = ScheduleSlotDb.findById(request.getSlot_id());
            if (scheduleSlotOptional.isEmpty()) {
                return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Слот расписания не найден"));
            }
            ScheduleSlot slot = scheduleSlotOptional.get();
            String executor;
            if(!request.getExecutor_id().equals(administratorId) && request.getExecutor_id()!=""){
                Optional<Employee> executorOptional = EmployeeDb.findById(request.getExecutor_id());
                if (executorOptional.isEmpty()) {
                    return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Исполнитель не найден"));
                }
                executor= request.getExecutor_id();
            }
            else executor=null;
            newPeriod.setScheduleId(request.getSchedule_id());
            newPeriod.setSlot_id(request.getSlot_id());
            newPeriod.setSlot_type(request.getSlot_type());
            newPeriod.setAdministrator_id(administratorId);
            newPeriod.setExecutor_id(executor);

            if (!isOverlapping(newPeriod, slot.getBegin_time(), slot.getEnd_time())) {
                return ResponseEntity.status(409).contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Период перекрывается с существующим периодом"));
            }
            SchedulePeriodDb.save(newPeriod);
            schedule.setUpdate_date(Instant.now());
            ScheduleDb.save(schedule);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Период успешно создан");
        }catch (Exception error) {
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    };

    @GetMapping("/getPeriod")
    @Operation(
            summary = "Получение периода расписания"
    )
    public ResponseEntity<?> getPeriod(@Valid @RequestParam String periodId){
        try {
            Optional<SchedulePeriod> period = SchedulePeriodDb.findById(periodId);
            if (period.isEmpty()){
                ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Периода с указанным id не существует");
            }
            SchedulePeriod periodRes = period.get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(periodRes);
        }catch (Exception error) {
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    };

    private boolean isOverlapping(SchedulePeriod newPeriod, OffsetTime begin_time, OffsetTime end_time) {
        Optional<List<SchedulePeriod>> OptOverlappingPeriods = SchedulePeriodDb.findAllByScheduleId(
                newPeriod.getScheduleId()
        );
        List<SchedulePeriod> overlappingPeriods = OptOverlappingPeriods.get();
        for(SchedulePeriod overlappingPeriod : overlappingPeriods)
        {
            Optional<ScheduleSlot> existingSlotOptional = ScheduleSlotDb.findById(overlappingPeriod.getSlot_id());
            ScheduleSlot existingSlot = existingSlotOptional.get();
            if(!((existingSlot.getEnd_time().isAfter(end_time) && existingSlot.getBegin_time().isAfter(begin_time))||(existingSlot.getEnd_time().isBefore(end_time) && existingSlot.getBegin_time().isBefore(begin_time))))
            {
                return false;
            };
        }
        return true;
    }
}
