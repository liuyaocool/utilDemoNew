package com.liuyao.demo.controller;

import com.liuyao.demo.utilutil.office.ExcelUtil2;
import com.liuyao.demo.utils.IOUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/file")
public class ImportController {

    @GetMapping("/page")
    public String login(){
        return "login";
    }

    @GetMapping("/fileUpload")
    public String filePage(){
        return "fileUpload";
    }

    @PostMapping("/import")
    public List<Map<String,String>> fileImport(@PathVariable("file") MultipartFile file){

        Map<Integer, String > columns = new HashMap<>();
        columns.put(1, "name");
        columns.put(2, "age");
        columns.put(3, "id");
        columns.put(4, "sex");
        columns.put(5, "hobby");
        columns.put(7, "beizhu");

        return ExcelUtil2.importExcel(file, columns, "yyyy-MM-dd HH:mm:ss", 3);
    }
    @PostMapping("/upload")
    public String fileUpload(@PathVariable("file") MultipartFile file, HttpServletRequest request){
        try {
            file.transferTo(new File("c:/" + request.getParameter("filename")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return IOUtil.upload(file, "c:/java/upload");
    }

    @PostMapping("/upWord")
    public String upWord (HttpServletRequest request, @PathVariable("word") MultipartFile word) throws IOException {

        return ExcelUtil2.getWordBook(word);
    }

}
