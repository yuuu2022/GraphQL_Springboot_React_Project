package com.example.training.training_project.fetcher;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.training.training_project.entity.UserEntity;
import com.example.training.training_project.mapper.UserEntityMapper;
import com.example.training.training_project.type.User;
import com.example.training.training_project.type.UserInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DgsComponent
public class UserDataFetcher {
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDataFetcher(UserEntityMapper userEntityMapper , PasswordEncoder passwordEncoder) {
        this.userEntityMapper = userEntityMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @DgsMutation
    public User createUser(@InputArgument UserInput userInput){
        ensureUserNotExists(userInput);

        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setEmail(userInput.getEmail());
        newUserEntity.setPassword(passwordEncoder.encode(userInput.getPassword()));
        userEntityMapper.insert(newUserEntity);

        newUserEntity.setPassword(null); // clear password 
        return User.fromEntity(newUserEntity);
    }

    private void ensureUserNotExists(UserInput userInput){
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getEmail , userInput.getEmail());
        if(userEntityMapper.selectCount(queryWrapper) >=1){
            throw new RuntimeException("the email had been used !");
        }
    }
}
