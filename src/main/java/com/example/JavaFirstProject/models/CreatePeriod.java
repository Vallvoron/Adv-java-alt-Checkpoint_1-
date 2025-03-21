package com.example.JavaFirstProject.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePeriod {

    @Schema(description = "Id прикрепляемого слота", example = "3d22234cfa4e4020891e9529b6e53596")
    @Size(min = 32, max = 32, message = "Id слота должен быть ровно 32 символа")
    @NotBlank(message = "Id слота не должен быть пустым")
    private String slot_id;

    @Schema(description = "Id прикрепляемого расписания", example = "3d22234cfa4e4020891e9529b6e53596")
    @Size(min = 32, max = 32, message = "Id расписания должен быть ровно 32 символа")
    @NotBlank(message = "Id расписания не должен быть пустым")
    private String schedule_id;

    @Schema(description = "Тип слота", example = "UNDEFINED")
    @Size(max = 20, message = "Тип слота не может быть больше 20 символов")
    private type slot_type;

    @Schema(description = "Id исполнителя слота", example = "3d22234cfa4e4020891e9529b6e53596")
    @Size(min = 32, max = 32, message = "Id исполнителя должен быть ровно 32 символа")
    private String executor_id;
}
