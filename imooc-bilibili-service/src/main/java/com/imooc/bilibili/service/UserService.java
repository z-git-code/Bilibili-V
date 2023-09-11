package com.imooc.bilibili.service;

import com.imooc.bilibili.dao.UserDao;
import com.imooc.bilibili.domain.User;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.domain.constant.UserConstant;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.service.util.MD5Util;
import com.imooc.bilibili.service.util.RSAUtil;
import com.imooc.bilibili.service.util.TokenUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service//我的这个类其实是服务层的一个类，同时@Service也会在SpringBoot构建的时候 系统会自动地把UserService注入进去 这样我们就可以在实际的阶段直接调用 就不需要我们再自己生成了
public class UserService {

    @Autowired
    private UserDao userDao;

    public void addUser(User user) {
        String phone = user.getPhone();
        //首先我们要判断这个手机号是不是合法的手机号
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空！");
        }
        //调用getuserbyphone这个方法
        User dbUser = this.getUserByPhone(phone);
        if (dbUser != null) {
            throw new ConditionException("改手机号已经注册！");
        }

        //主要的注册逻辑
        Date now = new Date();//之所以是要获取当前系统的时间戳是因为用户的密码需要进行一个MD5的加密。所以这里呢通过时间戳生成一个盐值，然后配合MD5一起给用户的密码进行加密
        //生成盐值
        String salt = String.valueOf(now.getTime());
        //获取从前端传递过来的密码
        String password = user.getPassword();//被前端经过RSA加密才传过来的？ --看之前密码篇应该就能理解了 --think:用户从前端输入密码 不可能不经过加密直接传到后端吧
        // 所以就用了RSA 到数据库就换了一个加密方法而已
        String rawPassword;//原始密码 明文密码
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        //密码成功解密出来后就可以进行MD5的加密了
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        //往数据库插入
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);
        userDao.addUser(user);
        //--创建完用户之后 把创建好的用户的ID拿出来 然后再创建跟这个ID的用户信息 xml: useGeneratedKeys, keyproperty
        // ->这两个属性其实告诉数据库我们在创建完之后是需要拿到user表里生成的主键的、

        //添加用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        //系统预设信息--初次注册
        userInfo.setNick(UserConstant.DEFAULT_NICK);//3-9 14：37好处：一旦我这个方法多了， 就是说调用setNICK这个方法在各种文件都有调用到 那如果写死的话
        // 如果需要修改 就需要好多地方一起修改 如果统一集中在UserConstant里 那涉及到修改的时候只需要在UserConstant里修改就可以了 而且代码会更加优雅一些
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setCreateTime(now);

        //添加userInfo实体类到数据库里面
        userDao.addUserInfo(userInfo);//它的入参是UserInfo类的

    }

    //第二判断这个手机号对应的用户有没有已经注册到我们的系统去了
    public User getUserByPhone(String phone) {
        return userDao.getUserByPhone(phone);
    }

    public String login(User user) throws Exception {
        String phone = user.getPhone() == null ? "" : user.getPhone();
        String email = user.getEmail() == null ? "" : user.getEmail();
        if (StringUtils.isNullOrEmpty(phone) && StringUtils.isNullOrEmpty(email)) {//you can think why teacher uses "and" instead of "or"
            throw new ConditionException("参数异常！");
        }
        User dbUser = userDao.getUserByPhoneOrEmail(phone, email);
        if (dbUser == null) {
            throw new ConditionException("当前用户不存在！");
        }

        //判断用户的密码和数据库里的是否匹配
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }
        //对密码进行md5的加密
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        //比对密码是不是和dbUser里放的密码是一致的就ok了
        if (!md5Password.equals(dbUser.getPassword())) {
            throw new ConditionException("密码错误");
        }
        //到这一步 判断为合法用户 最后生成用户令牌 返回给前端 --> TokenUtil

        return TokenUtil.generateToken(dbUser.getId());//3-12 下 一 08:49
        //throw
    }

    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo userInfo = userDao.getUserInfoByUserId(userId);

        //把user和userInfo进行整合
        //方法一 map， 方法二 User里添加冗余字段
        user.setUserInfo(userInfo);
        return user;
    }

    public void updateUsers(User user) throws Exception{
        //my version lacks of some necessary details
        Long id = user.getId();
        User dbUser = userDao.getUserById(id);
        if(dbUser == null){
            throw new ConditionException("用户不存在！");
        }
        if(!StringUtils.isNullOrEmpty(user.getPassword())){//I got the pwd of the user
            //At UserApi: userService.updateUsers(user), you want to write userService.updateUsers(userId)
            //If you pass the parameter 'userId' instead of 'user', I think it's difficult for us to use setPassword
            //and setUpdateTime.
            String rawPassword = RSAUtil.decrypt(user.getPassword());
            String md5Password = MD5Util.sign(rawPassword, dbUser.getSalt(), "UTF-8");
            user.setPassword(md5Password);
        }
        user.setUpdateTime(new Date());//why?
        userDao.updateUsers(user);
    }

    public void updateUserInfos(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        userDao.updateUserInfos(userInfo);
    }


}
