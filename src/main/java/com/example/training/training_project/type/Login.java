package com.example.training.training_project.type;

import lombok.Data;

@Data
public class Login {
    private Integer userId;
    private String token;
    private Integer tokenExpiration;
}
