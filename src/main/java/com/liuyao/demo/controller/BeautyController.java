package com.liuyao.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BeautyController {


    @GetMapping("/fileUpload")
    public String filePage(){
        return "fileUpload";
    }

}
