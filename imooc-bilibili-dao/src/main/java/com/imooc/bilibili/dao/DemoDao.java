package com.imooc.bilibili.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
//让项目在启动的时候自动地去匹配 把dao的文件封装成一个实体类  这就是为什么我们使用interface的方法 mybatis会自动帮我们给他进行一个实例化的操作
public interface DemoDao {
    public Long query(Long id);
}
