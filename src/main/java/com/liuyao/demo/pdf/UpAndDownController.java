package com.liuyao.demo.pdf;

import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("ud")
@Controller
public class UpAndDownController {

    @RequestMapping("upload.do")
    @ResponseBody
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "200");
        try {
            PdfConvertUtil pdfConvertUtil = new PdfConvertUtil();
            String pdfName = file.getOriginalFilename();
            int lastIndex = pdfName.lastIndexOf(".pdf");
            String fileName = pdfName.substring(0, lastIndex);
            String htmlName = fileName + ".html";
            String realPath = ResourceUtils.getURL("classpath:").getPath() + "/templates/file";
            File f = new File(realPath);
            if (!f.exists()) {
                f.mkdirs();
            }
            String htmlPath = realPath + "\\" + htmlName;
            pdfConvertUtil.pdftohtml(file.getBytes(), htmlPath);
        } catch (Exception e) {
            map.put("code", "500");
            e.printStackTrace();
        }
        return map;
    }

    public static void main(String[] args) throws IOException {
        File pdfFile = new File("C:\\Users\\liuyao\\Desktop\\test1.pdf");
        FileInputStream fileInputStream = new FileInputStream(pdfFile);
        MultipartFile mfile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        new UpAndDownController().upload(mfile, null);
    }

}
