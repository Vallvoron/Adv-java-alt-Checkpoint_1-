package com.example.JavaFirstProject.controllers;


import com.example.JavaFirstProject.entitys.*;
import com.example.JavaFirstProject.models.*;
import com.example.JavaFirstProject.repositories.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.OffsetTime;
import java.util.*;
import java.util.stream.Collectors;

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
            newschedule.setScheduleName(request.getSchedule_name());
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

    @GetMapping(value = "/getPeriods", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение периодов расписания с фильтрацией и пагинацией")
    public ResponseEntity<?> getPeriods(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String slotId,
            @RequestParam(required = false) String scheduleId,
            @RequestParam(required = false) String slotType,
            @RequestParam(required = false) String administratorId,
            @RequestParam(required = false) String executorId,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            List<SchedulePeriod> periodList = SchedulePeriodDb.findAll();

            periodList = periodList.stream().filter(period -> {
                boolean idMatch = true;
                boolean scheduleIdMatch = true;
                boolean slotIdMatch = true;
                boolean slotTypeMatch = true;
                boolean administratorIdMatch = true;
                boolean executorIdMatch = true;

                if (id != null && !id.isBlank()) {
                    idMatch = (period.getId() != null && period.getId().contains(id));
                }
                if (scheduleId != null && !scheduleId.isBlank()) {
                    scheduleIdMatch = (period.getScheduleId() != null && period.getScheduleId().contains(scheduleId));
                }
                if (slotId != null && !slotId.isBlank()) {
                    slotIdMatch = (period.getSlot_id() != null && period.getSlot_id().contains(slotId));
                }
                if (slotType != null && !slotType.isBlank()) {
                    slotTypeMatch = (period.getSlot_type() != null && period.getSlot_type().toString().contains(slotType));
                }
                if (administratorId != null && !administratorId.isBlank()) {
                    administratorIdMatch = (period.getAdministrator_id() != null && period.getAdministrator_id().contains(administratorId));
                }
                if (executorId != null && !executorId.isBlank()) {
                    executorIdMatch = (period.getExecutor_id() != null && period.getExecutor_id().contains(executorId));
                }

                return idMatch && scheduleIdMatch && slotIdMatch && slotTypeMatch && administratorIdMatch && executorIdMatch;
            }).collect(Collectors.toList());

            if (sortField == null || sortField.isEmpty()) {
                sortField = "id";
            }

            String finalSortField = sortField;
            Comparator<SchedulePeriod> comparator = (p1, p2) -> {
                try {

                    String getterName = "get" + StringUtils.capitalize(finalSortField);

                    Method method1 = SchedulePeriod.class.getMethod(getterName);
                    Method method2 = SchedulePeriod.class.getMethod(getterName);

                    Object value1 = method1.invoke(p1);
                    Object value2 = method2.invoke(p2);

                    if (value1 == null && value2 == null) {
                        return 0;
                    } else if (value1 == null) {
                        return -1;
                    } else if (value2 == null) {
                        return 1;
                    } else if (value1 instanceof Comparable) {
                        return ((Comparable) value1).compareTo(value2);
                    } else {

                        System.out.println("Поле " + finalSortField + " не реализует Comparable");
                        return 0;
                    }

                } catch (Exception e) {
                    System.out.println("Ошибка при сортировке по полю " + finalSortField + ": " + e.getMessage());
                    return 0;
                }
            };

            if (sortDirection != null && sortDirection.equalsIgnoreCase("DESC")) {
                comparator = comparator.reversed();
            }

            periodList = periodList.stream().sorted(comparator).collect(Collectors.toList());

            int totalCount = periodList.size();
            int totalPages = (int) Math.ceil((double) totalCount / size);
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, periodList.size());

            if (fromIndex >= periodList.size()) {
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Вы вышли за пределы списка"));
            }

            List<SchedulePeriod> paginatedList = periodList.subList(fromIndex, toIndex);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Map.of(
                    "totalCount", totalCount,
                    "totalPagesCount", totalPages,
                    "currentPage", page,
                    "pageSize", size,
                    "list", paginatedList));

        } catch (Exception error) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    }

    @GetMapping(value = "/getFullSchedule")
    @Operation(summary = "Получения полных параметров расписания вместе с отсортированными периодами")
    public ResponseEntity<?> getFullSchedule(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name
    ) {
        try {
            if (id == null && name == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Необходимо указать id или name"));
            }
            Optional<Schedule> optSchedule = (id != null)
                    ? ScheduleDb.findById(id)
                    : ScheduleDb.findByScheduleName(name);
            if (optSchedule.isEmpty()) {
                optSchedule = ScheduleDb.findByScheduleName(name);
                if(optSchedule.isEmpty()){
                    return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Расписание не найдено ни по одному параметру"));
                }
            }
            Schedule schedule = optSchedule.get();
            if(!Objects.equals(schedule.getScheduleName(), name) && name!=null){
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Имя не соответствует расписанию с указанным id"));
            }

            Optional<List<SchedulePeriod>> optPeriods = SchedulePeriodDb.findAllByScheduleId(schedule.getId());
            List<SchedulePeriod> periods;
            periods = optPeriods.orElse(null);
            if(periods== null)
            {
                Map<String, Object> response = Map.of(
                        "schedule", schedule,
                        "periods", "[]"
                );

                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
            }
            List<SchedulePeriod> sortedPeriods = periods.stream()
                    .sorted(Comparator.comparing(period -> {
                        ScheduleSlot slot = ScheduleSlotDb.findById(period.getSlot_id()).orElse(null);
                        return (slot != null) ? slot.getBegin_time() : null;
                    }, Comparator.nullsLast(Comparator.naturalOrder())))
                    .toList();

            Map<String, Object> response = Map.of(
                    "schedule", schedule,
                    "periods", sortedPeriods
            );

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception error) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    }
    /*@PostMapping(value = "/getPeriods", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение периодов расписания с фильтрацией, сортировкой и пагинацией")
    public ResponseEntity<?> getPeriods(
            @RequestBody(required = false) Filter filter,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            List<SchedulePeriod> periodList = SchedulePeriodDb.findAll();

            if (filter != null) {
                periodList = periodList.stream().filter(period -> {
                    boolean idMatch = true;
                    boolean scheduleIdMatch = true;
                    boolean slotIdMatch = true;
                    boolean slotTypeMatch = true;
                    boolean administratorIdMatch = true;
                    boolean executorIdMatch = true;

                    if (filter.getId() != null && !filter.getId().isBlank()) {
                        idMatch = (period.getId() != null && period.getId().contains(filter.getId()));
                    }
                    if (filter.getScheduleId() != null && !filter.getScheduleId().isBlank()) {
                        scheduleIdMatch = (period.getScheduleId() != null && period.getScheduleId().contains(filter.getScheduleId()));
                    }
                    if (filter.getSlotId() != null && !filter.getSlotId().isBlank()) {
                        slotIdMatch = (period.getSlot_id() != null && period.getSlot_id().contains(filter.getSlotId()));
                    }
                    if (filter.getSlotType() != null && !filter.getSlotType().isBlank()) {
                        slotTypeMatch = (period.getSlot_type() != null && period.getSlot_type().toString().contains(filter.getSlotType()));
                    }
                    if (filter.getAdministratorId() != null && !filter.getAdministratorId().isBlank()) {
                        administratorIdMatch = (period.getAdministrator_id() != null && period.getAdministrator_id().contains(filter.getAdministratorId()));
                    }
                    if (filter.getExecutorId() != null && !filter.getExecutorId().isBlank()) {
                        executorIdMatch = (period.getExecutor_id() != null && period.getExecutor_id().contains(filter.getExecutorId()));
                    }

                    return idMatch && scheduleIdMatch && slotIdMatch && slotTypeMatch && administratorIdMatch && executorIdMatch;
                }).collect(Collectors.toList());
            }

            if (sortField != null && !sortField.isEmpty()) {
                if (sortDirection != null && sortDirection.equalsIgnoreCase("ASC")) {
                    periodList = periodList.stream()
                            .sorted((p1, p2) -> {
                                if (sortField.equalsIgnoreCase("id")) {
                                    return p1.getId().compareTo(p2.getId());
                                } else if (sortField.equalsIgnoreCase("scheduleId")) {
                                    return p1.getScheduleId().compareTo(p2.getScheduleId());
                                }
                                return 0;
                            })
                            .collect(Collectors.toList());
                } else {
                    periodList = periodList.stream()
                            .sorted((p1, p2) -> {
                                if (sortField.equalsIgnoreCase("id")) {
                                    return p2.getId().compareTo(p1.getId());
                                } else if (sortField.equalsIgnoreCase("scheduleId")) {
                                    return p2.getScheduleId().compareTo(p1.getScheduleId());
                                }
                                return 0;
                            })
                            .collect(Collectors.toList());
                }
            }

            int totalCount = periodList.size();
            int totalPages = (int) Math.ceil((double) totalCount / size);
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, periodList.size());

            if (fromIndex >= periodList.size()) {
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "Вы вышли за пределы списка"));
            }

            List<SchedulePeriod> paginatedList = periodList.subList(fromIndex, toIndex);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Map.of(
                    "totalCount", totalCount,
                    "totalPagesCount", totalPages,
                    "currentPage", page,
                    "pageSize", size,
                    "list", paginatedList));

        } catch (Exception error) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Ошибка: " + error.getMessage()));
        }
    }*/

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
