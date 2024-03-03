package com.example.training.training_project.custom;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import com.example.training.training_project.entity.UserEntity;
import com.example.training.training_project.mapper.UserEntityMapper;
import com.example.training.training_project.util.TokenUtil;
import com.netflix.graphql.dgs.context.DgsCustomContextBuilderWithRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthContextBuilder implements DgsCustomContextBuilderWithRequest{

    private final UserEntityMapper userEntityMapper;
    static final String AUTHORIZATION_HEADER = "Authorization";

    public AuthContextBuilder(UserEntityMapper userEntityMapper){
        this.userEntityMapper = userEntityMapper;
    }
    
    @Override
    public Object build(Map map, HttpHeaders httpHeaders, WebRequest webRequest) {
        // TODO Auto-generated method stub
        log.info("Building Auth context ...");
        AuthContext authContext = new AuthContext();
        if(!httpHeaders.containsKey(AUTHORIZATION_HEADER)){
            return authContext;
        }

        String authorization = httpHeaders.getFirst(AUTHORIZATION_HEADER);
        String token = authorization.replace("Bearer ", "");

        Integer userId;
        try{
            userId = TokenUtil.verifyToken(token);
        }catch(Exception ex){
            authContext.setTokenInvalid(true);
            return authorization;
        }

        UserEntity userEntity = userEntityMapper.selectById(userId);
        if(userEntity == null){
            authContext.setTokenInvalid(true);
            return authContext;
        }
        authContext.setUserEntity(userEntity);
        log.info("The user have been authorizated, userId = {}",userId);
        
        return authContext;
    }
    
}
