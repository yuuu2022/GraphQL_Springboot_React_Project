package com.example.training.training_project.util;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenUtil {
    static final long MILLI__SECONDS_IN_HOUR = 1*60*60*1000;
    static final String issuer = "thisistesting";
    static final String USER_ID = "userId";
    static Algorithm algorithm = Algorithm.HMAC256("secretkey");

    public static String signToken(Integer userId,int expirationInHour){
        String token = JWT.create().withIssuer(issuer).withClaim(USER_ID, userId).
                        withExpiresAt(new Date(System.currentTimeMillis() + expirationInHour * MILLI__SECONDS_IN_HOUR))
                        .sign(algorithm);
        return token;
    }

    public static Integer verifyToken(String token){
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        Integer userId = decodedJWT.getClaim(USER_ID).asInt();
        return userId;
    }
}
