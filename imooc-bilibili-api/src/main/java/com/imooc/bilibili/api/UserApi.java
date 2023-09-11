package com.imooc.bilibili.api;

import com.imooc.bilibili.api.support.UserSupport;
import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.domain.User;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.service.UserService;
import com.imooc.bilibili.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserApi {

    @Autowired//“我把刚才新建的UserService通过注解的形式给引入进来 这个注解呢就是 在SpringBoot中用来引入相关依赖 或者说相关实体类 这样的一个方法 // 会遇到一个问题 但老师现在还不说
    private UserService userService;
    /*private final UserService userService;

    @Autowired
    public UserApi(UserService userService){
        this.userService = userService;
    }
    */

    @Autowired
    private UserSupport userSupport;

    @GetMapping("/users")
    public JsonResponse<User> getUserInfo() {
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getUserInfo(userId);
        return new JsonResponse<>(user);
    }

    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey() {
        String pk = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(pk);
    }

    @PostMapping("/users")//新建一个用户
    public JsonResponse<String> addUser(@RequestBody User user) {//这个body是把我们这个user 在前端传递的时候进行封装 封装成一个Json类型 然后提供给我们
        userService.addUser(user);
        return JsonResponse.success();//?为什么不需要判断成功还是失败呢？ 因为我们会在adduser方法里提前把可能失败的原因 全都写好 而且在失败的瞬间就会立刻抛出异常
        // 所以如果走到了最后一步应该是没有什么问题的
    }//这个body是把我们这个user在前端传？去的时候进行封装 封装成一个JSON类型  提供给我们

    @PostMapping("/user-tokens")//why tokens -- 登录成功之后有用户凭证（用户令牌） // 实际上我们登录是请求用户令牌这个资源
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

    @PutMapping("/users")
    public JsonResponse<String> updateUsers(@RequestBody User user) throws Exception{
        //to be continued
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);//identify user? -> you can think when you want to update your profile, how the
        //App will do
        userService.updateUsers(user);
        return JsonResponse.success();//new JsonResponse<user>
    }

    @PutMapping("/user-infos")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo) {
        //userId一般都是从token获取到的，而不是前端传给我们的。如果前端直接传给我们，可能会被拦截，然后仿造一个相同用户Id或者其他用户Id，被攻击者来利用我们的接口，获取到用户的一些相关的信息
        //所以我们的userId都是从我们的token中获取的，因为我们的token是没办法轻易伪造的。就算是被拦截，token也有有效期。过了有效期， 攻击者就不能使用了
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfos(userInfo);
        return JsonResponse.success();
    }

}
