package com.imooc.bilibili.api.support;

import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.service.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserSupport {

    public Long getCurrentUserId() {
        //通用方法的功效呢就是从我们的请求头里边统一拿前端给我们的token 进行解析 解析的方法就是verifyToken； 3-13（下（二））2：24
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();//这个类呢其实是SpringBoot框架里面
        //给我们抓取请求上下文的 相关的这种方法 可以通过这些方法来获取到相关的我们想要的一些请求封装好的信息

        String token = requestAttributes.getRequest().getHeader("token");
        Long userId = TokenUtil.verifyToken(token);
        if(userId < 0){
            throw new ConditionException("非法用户！");
        }
        return userId;
    }

}
