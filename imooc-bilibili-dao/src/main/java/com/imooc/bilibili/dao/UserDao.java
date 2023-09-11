package com.imooc.bilibili.dao;

import com.imooc.bilibili.domain.User;
import com.imooc.bilibili.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper//与数据库进行交互 //这是属于Mybatis的一个注解 当一个interface被标注了此注解之后  它就可以跟Mybatis产生 一个关联 然后Mybatis就可以把  它管理的xml跟相关的实体类进行一个关联 那么我在实体类写的方法就会自动映射到Mybatis的xml文件中去
//这样就完整的形成和数据库交互的逻辑了
public interface UserDao {
    User getUserByPhone(String phone);//接下来我们就需要再userdao对应的xml文件里面去写对应的sql语句了

    Integer addUser(User user); //Integer：它成功？数据的数量？  // 有些方法也有可能会用到

    Integer addUserInfo(UserInfo userInfo);

    User getUserById(Long id);

    UserInfo getUserInfoByUserId(Long userId);

    Integer updateUserInfos(UserInfo userInfo);

    Integer updateUsers(User user);

    User getUserByPhoneOrEmail(@Param("phone") String phone, @Param("email") String email);
}
//首先从API控制层 也就是我们的接口层 前端访问到我们的接口之后 会跳转到相关的业务实现逻辑 也就是我们的service层 然后在service层要用到和数据库的交互 这时候service就回去访问dao的功能 dao与mybaitis产生关联 通过mybatis与
//数据库进行交互 交互到的结果会返回给service service再返回给api