package com.example.project_exemple.main.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private List<ValidationError> errors;
}