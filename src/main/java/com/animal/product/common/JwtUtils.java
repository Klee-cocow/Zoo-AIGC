package com.animal.product.common;

import com.animal.product.exception.BusinessException;
import com.animal.product.model.vo.UserVO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.HashMap;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/21 1:10
 */
public class JwtUtils {

    //jwt生成令牌
    public static String generateToken(UserVO user){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,7);

        String token = JWT.create()
                .withHeader(new HashMap<>())
                .withClaim("Name",user.getName())
                .withClaim("Email",user.getEmail())
                .withClaim("Phone",user.getPhone())
                .withClaim("Avatar",user.getAvatar())
                .withClaim("Money",user.getMoney())
                .withClaim("Description",user.getDescription())
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256("!ZOOPARTY"));

        return token;
    }

    //校验jwt
    public static Boolean checkToken(String jwtToken){
        if(StringUtils.isEmpty(jwtToken)) return false;
        DecodedJWT decode = JWT.decode(jwtToken);

        long expirationTime = decode.getExpiresAt().getTime();
        if(System.currentTimeMillis() > expirationTime){
            return false;
        }

        return true;
    }
}
