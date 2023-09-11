package com.imooc.bilibili.service.handler;


import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.exception.ConditionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)//优先级最高
public class CommonGlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)//只要是抛出了一个异常，我都要用它这个方法进行处理
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request, Exception e){//HttpServletRequest：封装从前端获得到的请求的
        // 一般前端传的这个请求里都有哪些内容呢 比如说请求头 一些请求信息 我们都可以通过HttpServletRequest进行封装然后获取到
        String errorMsg = e.getMessage();
        if(e instanceof ConditionException){
            String errorCode = ((ConditionException)e).getCode();
            return new JsonResponse<>(errorCode, errorMsg);
        }else{
            return new JsonResponse<>("500", errorMsg);
        }
    }
}
