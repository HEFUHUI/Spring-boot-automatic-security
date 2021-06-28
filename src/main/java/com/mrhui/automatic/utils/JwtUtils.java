package com.mrhui.automatic.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.service.TUserService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    public static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;
    private static final long TOKEN_EXPIRES =  1000*60*60*24;

    public static final String SECRET = "Hello";
    /**
     * 令牌生成
     * @param user 用户信息
     * @return 令牌
     */
    public static String getToken(TUser user){
        return JWT.create()
                .withClaim("loginName",user.getLoginName())
                .withClaim("userId",user.getUserId())
                .withAudience(user.getPassword())
                .withExpiresAt(new Date(System.currentTimeMillis()+TOKEN_EXPIRES))
                .sign(Algorithm.HMAC256(SECRET));
    }


    /**
     *获得token的字段 claim
     * @param token 用户令牌
     * @param filed 字段名
     * @return 字段内容
     */
    public static String getClaimFiled(String token, String filed) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(filed).asString();
        } catch (JWTDecodeException | NullPointerException e) {
            return null;
        }
    }


    /**
     * 刷新用户令牌的过期时间
     * @param token 用户令牌
     * @return 新的用户令牌
     */
    public static String refreshTokenExpired(String token){
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        try {
            Date date = new Date(System.currentTimeMillis() + TOKEN_EXPIRES);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTCreator.Builder builer = JWT.create().withExpiresAt(date);
            for (Map.Entry<String, Claim> entry : claims.entrySet()) {
                builer.withClaim(entry.getKey(), entry.getValue().asString());
            }
            return builer.sign(algorithm);
        } catch (JWTCreationException e) {
            return null;
        }
    }

    /**
     * 令牌校验
     * @param token 用户令牌
     * @return 用户ID
     */
    public static String sign(String token){
        final JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        try {
            return jwtVerifier.verify(token).getClaim("userId").asString();
        }catch (JWTVerificationException e){
            return null;
        }
    }
}
