package com.example.JavaFirstProject.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateEmployee {
    @Schema(description = "Имя сотрудника", example = "name")
    @Size(max = 255, message = "Имя не может быть больше 255 символов")
    @NotBlank(message = "Имя не должно быть пустым")
    private String employee_name;

    @Schema(description = "Статус сотрудника", example = "WORKING")
    @Size(max = 20, message = "Статус не может быть больше 20 символов")
    @NotBlank(message = "Статус не должен быть пустым")
    private emp_status status;

    @Schema(description = "Позиция сотрудника", example = "UNDEFINED")
    @Size(max = 20, message = "Позиция не может быть больше 20 символов")
    private emp_position position;
}
