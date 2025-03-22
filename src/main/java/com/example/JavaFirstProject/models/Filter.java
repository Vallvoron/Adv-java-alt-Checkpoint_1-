package com.example.JavaFirstProject.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Filter {
    private String id;
    private String slotId;
    private String scheduleId;
    private String slotType;
    private String administratorId;
    private String executorId;
}
