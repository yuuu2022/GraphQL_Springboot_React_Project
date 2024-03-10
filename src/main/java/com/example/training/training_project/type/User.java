package com.example.training.training_project.type;

import java.util.ArrayList;
import java.util.List;

import com.example.training.training_project.entity.EventEntity;
import com.example.training.training_project.entity.UserEntity;
import com.example.training.training_project.util.DateUtil;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String password;
    private List<Event> createdEvents = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    public static User fromEntity(UserEntity userEntity){
        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        return user;
    }
}
