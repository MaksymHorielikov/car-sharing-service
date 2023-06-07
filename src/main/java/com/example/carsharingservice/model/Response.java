package com.example.carsharingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private String timestamp;
    private String message;
    private int status;
}
