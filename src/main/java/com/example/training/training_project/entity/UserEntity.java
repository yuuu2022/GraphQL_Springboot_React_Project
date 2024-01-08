package com.example.training.training_project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.training.training_project.type.EventInput;
import com.example.training.training_project.type.UserInput;
import com.example.training.training_project.util.DateUtil;

import lombok.Data;

@Data
@TableName(value="tb_user")
public class UserEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String email;
    private String password;

    public static UserEntity fromUserEntity(UserInput userInputs){
        UserEntity newEvent = new UserEntity();
        newEvent.setEmail(userInputs.getEmail());
        newEvent.setPassword(userInputs.getPassword());
        return newEvent;
    }
}
