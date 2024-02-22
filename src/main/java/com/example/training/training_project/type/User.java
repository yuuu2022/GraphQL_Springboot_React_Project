package com.example.training.training_project.type;

import com.example.training.training_project.entity.EventEntity;
import com.example.training.training_project.entity.UserEntity;
import com.example.training.training_project.util.DateUtil;

import lombok.Data;

@Data
public class User {
    private String id;
    private String email;
    private String password;
    private String List<String> createEvents = new ArrayList(createEvent);

    public static User fromEntity(UserEntity userEntity){
        User user = new User();
        user.setId(userEntity.getId().toString());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        return user;
    }
}
