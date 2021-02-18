package com.liuyao.demo.controller;

import com.liuyao.demo.utils.ServletUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

//@CrossOrigin
@Controller
@RequestMapping("/main")
public class TestController {

    @GetMapping("/testId/{id}")
    @CrossOrigin
    @ResponseBody
    public String testId(@PathVariable("id") String id){
        return "hello" + id;
    }

    @GetMapping("/testId2")
    @ResponseBody
    public String testId2(String id){
        return "hello" + id;
    }

    @GetMapping("/page")
    public String test(Model model){
        model.addAttribute("param", "aaa");
        return "main";
    }

    //跨域通信 可加在类上 不加参数所有可通过
    @CrossOrigin(origins = {"http://localhost:8980","null"})
    @RequestMapping(value = "/postform", method = RequestMethod.POST)
    @ResponseBody
    public String ttt(Model model, HttpServletRequest request, HttpServletResponse response) {
        return ServletUtil.getRemoteIps();
    }

    @RequestMapping(value = "/getform", method = RequestMethod.GET)
    @ResponseBody
    public String aaa(Model model, HttpServletRequest request, HttpServletResponse response) {
        return ServletUtil.getRemoteIps();
    }

    @RequestMapping(value = "/postajax", method = RequestMethod.POST)
    @ResponseBody
    public String sss(@RequestBody Map params, HttpServletRequest request, HttpServletResponse response) {
        return ServletUtil.getRemoteIps();
    }

    @RequestMapping(value = "/getajax", method = RequestMethod.GET)
    @ResponseBody
    public String bbb(@RequestBody Map params, HttpServletRequest request, HttpServletResponse response) {
        return ServletUtil.getRemoteIps();
    }

    @RequestMapping(value = "/{pa}/{param}", method = RequestMethod.POST)
    @ResponseBody
    public String tt(@PathVariable("pa") String pa,@PathVariable("param") String param,
            HttpServletRequest request, HttpServletResponse response) {
        System.out.println(new Date() +request.getRemoteAddr());
        return "123123";
    }

}
