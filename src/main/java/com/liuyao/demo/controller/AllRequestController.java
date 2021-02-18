package com.liuyao.demo.controller;

import com.liuyao.demo.utils.ServletUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/all")
public class AllRequestController {

    @GetMapping("/getPage")
    public String a(Model model){
        return "fileUpload";
    }

    @GetMapping("/getString")
    @ResponseBody
    public String e(){
        return "getString";
    }

    @GetMapping("/getJsonString")
    @ResponseBody
    public String b(){
        return "{code: 200, msg: \"get success\"}";
    }

    @GetMapping("/getJsonMap")
    @ResponseBody
    public Map g(){
        Map map = new HashMap();
        map.put("code", 200);
        map.put("msg", "get map success");
        return map;
    }

    @PostMapping("/postString")
    public String c(){
        return "postString";
    }

    @PostMapping("/postJsonString")
    @ResponseBody
    public String f(){
        return "postString";
    }

    @PostMapping("/postJson")
    @ResponseBody
    public String d(){
        return "{code: 200, msg: \"post success\"}";
    }

    @PostMapping("/postJsonMap")
    @ResponseBody
    public Map h(){
        ServletUtil.getAddr();
        Map map = new HashMap();
        map.put("code", 200);
        map.put("msg", "post map success");
        return map;
    }
}
