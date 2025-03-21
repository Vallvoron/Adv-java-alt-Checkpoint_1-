package com.example.JavaFirstProject.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetTime;

@Data
public class CreateSlot {
    @Schema(description = "Id шаблона расписания", example = "3d22234cfa4e4020891e9529b6e53596")
    @Size(min=32, max = 32, message = "Id должен быть ровно 32 символа")
    @NotBlank(message = "Id не должен быть пустым")
    private String schedule_template_id;

    @Schema(description = "Время начала слота", example = "10:15:30+01:00")
    @NotBlank(message = "Время начала не должно быть пустым")
    private OffsetTime begin_time;

    @Schema(description = "Время завершения слота", example = "10:15:30+01:00")
    @NotBlank(message = "Имя завершения не должно быть пустым")
    private OffsetTime end_time;
}
