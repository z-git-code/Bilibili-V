package com.imooc.bilibili.api;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RESTfulApi {

    private final Map<Integer, Map<String, Object>> dataMap;

    public RESTfulApi(){
        dataMap = new HashMap<>();
        for(int i = 1; i < 3; i++){
            Map<String, Object> data = new HashMap<>();
            data.put("id", i);
            data.put("name", "name" + i);
            dataMap.put(i, data);
        }
    }

    @GetMapping("/objects/{id}")//api address
    public Map<String, Object> getData(@PathVariable Integer id){
        return dataMap.get(id);
    }

    @DeleteMapping("/objects/{id}")
    public String deleteData(@PathVariable Integer id){
        dataMap.remove(id);
        return "delete success";
    }

    @PostMapping("/objects")
    public String postData(@RequestBody Map<String, Object> data){//Spring boot 会根据这个注解 把我们的请求参数data进行一个自动的封装 然后
        //以JSON的形式传输进来
        Integer[] idArray = dataMap.keySet().toArray(new Integer[0]);
        Arrays.sort(idArray);
        int nextId = idArray[idArray.length - 1] + 1;
        dataMap.put(nextId, data);
        return "post success";
    }

    @PutMapping("/objects")
    public String putData(@RequestBody Map<String, Object> data){
        Integer id = Integer.valueOf(String.valueOf(data.get("id")));//if you want to write this line: int id = data.get("id"), you'll get
        //Type mismatch: cannot convert from Object to int
        Map<String, Object> containedData = dataMap.get(id);
        if(containedData == null){
            Integer[] idArray = dataMap.keySet().toArray(new Integer[0]);
            Arrays.sort(idArray);
            int nextId = idArray[idArray.length - 1] + 1;
            dataMap.put(nextId, data);
        }else{
            dataMap.put(id, data);
        }
        return "put success";
    }

}
