package com.example.training.training_project.custom;
import com.example.training.training_project.entity.UserEntity;
import com.example.training.training_project.type.User;

import lombok.Data;

@Data
public class AuthContext {
    private UserEntity userEntity;
    private boolean tokenInvalid;

    public void ensureAuthenticated(){
        if(tokenInvalid) throw new RuntimeException("Token is not valid");
        if(userEntity == null) throw new RuntimeException("have not login. Please logon first!");
    }
}
