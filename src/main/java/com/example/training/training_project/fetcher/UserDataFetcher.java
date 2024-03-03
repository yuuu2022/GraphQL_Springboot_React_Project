package com.example.training.training_project.fetcher;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.training.training_project.entity.EventEntity;
import com.example.training.training_project.entity.UserEntity;
import com.example.training.training_project.mapper.EventEntityMapper;
import com.example.training.training_project.mapper.UserEntityMapper;
import com.example.training.training_project.type.AuthData;
import com.example.training.training_project.type.Event;
import com.example.training.training_project.type.LoginInput;
import com.example.training.training_project.type.User;
import com.example.training.training_project.type.UserInput;
import com.example.training.training_project.util.TokenUtil;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DgsComponent
public class UserDataFetcher {
    private final UserEntityMapper userEntityMapper;
    private final EventEntityMapper eventEntityMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDataFetcher(UserEntityMapper userEntityMapper , PasswordEncoder passwordEncoder, EventEntityMapper eventEntityMapper) {
        this.userEntityMapper = userEntityMapper;
        this.passwordEncoder = passwordEncoder;
        this.eventEntityMapper = eventEntityMapper;
    }

    @DgsQuery
    public List<User> users(){
        List<UserEntity> userEntities = userEntityMapper.selectList(null);
        List<User> users = userEntities.stream().map(User::fromEntity).collect(Collectors.toList());
        return users;
    }

    @DgsQuery
    public AuthData login (@InputArgument LoginInput loginInput){
        UserEntity userEntity = this.findUserByEmail(loginInput.getEmail());
        if(userEntity == null){
            throw new RuntimeException("The system has no this user");
        }
        
        boolean match = passwordEncoder.matches(loginInput.getPassword(), userEntity.getPassword());
        if(!match){
            throw new RuntimeException("The password is not match");
        }
        
        String token = TokenUtil.signToken(userEntity.getId(), 1);
        
        AuthData authData = new AuthData();
        authData.setUserId(userEntity.getId());
        authData.setToken(token);
        authData.setTokenExpiration(1);
        return authData;
    }

    @DgsMutation
    public User createUser(@InputArgument UserInput userInput){
        ensureUserNotExists(userInput);

        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setEmail(userInput.getEmail());
        newUserEntity.setPassword(passwordEncoder.encode(userInput.getPassword()));
        userEntityMapper.insert(newUserEntity);

        newUserEntity.setPassword(null); // clear password then return the objects
        return User.fromEntity(newUserEntity);
    }

    @DgsData(parentType="User", field="createdEvents")
    public List<Event> createEvents(DgsDataFetchingEnvironment dfe){
        User user = dfe.getSource();
        QueryWrapper<EventEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EventEntity::getCreatorId, user.getId());
        List<EventEntity> eventEntities = eventEntityMapper.selectList(queryWrapper);
        List<Event> events = eventEntities.stream().map(Event::fromEntity).collect(Collectors.toList());
        return events ;
    }

    private void ensureUserNotExists(UserInput userInput){
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getEmail , userInput.getEmail());
        if(userEntityMapper.selectCount(queryWrapper) >=1){
            throw new RuntimeException("the email had been used !");
        }
    }

    private UserEntity findUserByEmail(String email){
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getEmail,email);
        return userEntityMapper.selectOne(queryWrapper);
    }
}
