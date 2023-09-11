package com.imooc.bilibili.domain;

public class JsonResponse<T> {
    //返回状态码
    private String code;

    //返回提示语
    private String msg;

    //泛型 why ->返回的数据类型多种多样， 更加灵活
    private T data;

    //定制化的构造器方法 这样能让JsonResponse更加灵活
    public JsonResponse(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public JsonResponse(T data){
        this.data = data;
        msg = "成功";
        code = "0";//0代表成功，非零就是失败的意思
    }

    //内部public方法 然后来使用相关的构造方法
    public static JsonResponse<String> success(){
        return new JsonResponse<>(null);
    }

    public static JsonResponse<String> success(String data){
        return new JsonResponse<>(data);//-> 是上一个方法的补充：我需要给前端返回一些参数，而且它是字符串类型的 这个方法主要用于什么呢
        //比如说我们用户登录之后 系统会给用户返回一个令牌 那这个令牌肯定是字符串类型 这时候我们就可以调用这个方法， 直接把令牌作为data传递进来 然后返回给前端
    }

    public static JsonResponse<String> fail(){
        return new JsonResponse<>("1", "失败");
    }

    //把上面写死的code1还有失败变成可配的
    public static JsonResponse<String> fail(String code, String msg){
        return new JsonResponse<>(code, msg);//这个方法它主要用于一些我们需要返回给前端特定的状态码和提示信息
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
