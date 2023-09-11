package com.imooc.bilibili.exception;

public class ConditionException extends RuntimeException{//RuntimeException就是我们程序在运行中抛出的异常；它会继承RuntimeException,补充RuntimeException里没有的一些东西

    //给它序列化用的
    private static final long serialVersionUID = 1L;

    //对应jsonresponse 响应状态码
    private String code;

    public ConditionException(String code, String name){
        super(name);
        this.code = code;
    }

    public ConditionException(String name){
        super(name);//引用上一级的构造方法
        code = "500";//在企业及项目开发当中 对于常规的错误处理 都可以用500来返回给前端
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
