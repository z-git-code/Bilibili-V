package com.imooc.bilibili.service.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class JsonHttpMessageConverterConfig {

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        Object o = new Object();
        list.add(o);
        list.add(o);
        System.out.println(list.size());
        System.out.println(JSONObject.toJSONString(list));//把list里面的元素打印一下；把list这个类转换成JSON格式的字符串
        System.out.println(JSONObject.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect));
    }

    @Bean//这个annotation经常和@configuration来搭配使用// 也就是说在我这个配置文件里可以通过@Bean再向我们Spring Boot上下文里注入我们想要的实体类 让他们提前能够生效
    @Primary
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setSerializerFeatures(//对Json数据一些序列化相关的配置
                //格式化输出 按标准Json格式进行输出 包括缩进和换行
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue,
                //对key-value键值对进行排列, 默认是升序的排序
                SerializerFeature.MapSortField,

                SerializerFeature.DisableCircularReferenceDetect
        );//对JSON数据序列化的一些相关配置
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConverter);
    }
}
