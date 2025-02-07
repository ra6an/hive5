package com.hive5.hive5.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ApiResponse {
    private String message;
    private String status;
    private int statusCode = 200;
    private Map<String, Object> data;
}
