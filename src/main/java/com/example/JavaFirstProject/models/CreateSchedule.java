package com.example.JavaFirstProject.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateSchedule {

    @Schema(description = "Название расписания", example = "name")
    @Size(max = 255, message = "Имя не может быть больше 255 символов")
    @NotBlank(message = "Id не должен быть пустым")
    private String schedule_name;

}
