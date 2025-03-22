package com.example.JavaFirstProject.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Sort {
    private String field;
    private String direction;
}
