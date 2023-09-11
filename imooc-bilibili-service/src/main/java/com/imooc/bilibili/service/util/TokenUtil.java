package com.imooc.bilibili.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.imooc.bilibili.exception.ConditionException;

import java.util.Calendar;
import java.util.Date;

public class ·TokenUtil {

    private static final String ISSUER = "签发者";//大家可以写自己所属的机构 或者个人都可以

    public static String generateToken(Long userId) throws Exception {//why userId: 3-10 6:31
        //因为JWT需要一个加密算法，来最后给他进行一个加密
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());//why rsa : RSAUTIL // ->省去了自己再新生成一个密钥的步骤
        //方便我们生成JWT过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //超时时间
        calendar.add(Calendar.SECOND, 30);
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);//整体加密
    }

    //验证的方法
    public static Long verifyToken(String token) {//no directly throwing Exception reason:3-12(下(一)) 12：17
        try {
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);//这个JWT其实就跟我们在新建JWT的时候 已经新建成的未加密的JWT一样的了
            String userId = jwt.getKeyId();
            return Long.valueOf(userId);
        } catch (TokenExpiredException e){
            throw new ConditionException("555", "token过期！");//用户体验 无感 继续体验我们的系统；通用报错 -> 特殊报错 15：23
        }catch (Exception e){
            throw new ConditionException("非法用户token! ");//除了我们说到过的已经过期的token, 其他的情况我们都暂时归于非法token
        }

    }
}