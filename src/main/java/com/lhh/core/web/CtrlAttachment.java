package com.lhh.core.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lhh.base.config.PropertiesCore;
import com.lhh.base.utils.Result;

@Controller
@RequestMapping("/attachment")
public class CtrlAttachment {
    private final Logger log = LoggerFactory.getLogger(CtrlAttachment.class);
    @Autowired
    private PropertiesCore coreProperties;
    @RequestMapping("upload")
    @ResponseBody
    public Result upload(@RequestParam(name="file")MultipartFile file, String module) {
        Result result = Result.success();
        String originalFileName = file.getOriginalFilename();
        if(StringUtils.isBlank(originalFileName))
            return Result.failure("OriginalFileName is null");
        String type = originalFileName.substring(originalFileName.lastIndexOf(".")+1).toLowerCase();
        String fileName = UUID.randomUUID().toString()+"."+type;
        String filePath = getFilePath(module, fileName);
        try {
            saveFile(file.getInputStream(), filePath);
            result.put("msg","success");
            Map<String, String> map = new HashMap<>();
            map.put("fileName",fileName);
            result.put("data",map);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private String getFilePath(String module, String fileName) {
        String relativePath = "upload"+File.separator+module+File.separator+fileName;
        if(StringUtils.isBlank(coreProperties.getAttachmentHome())) {
            return relativePath;
        }
        return coreProperties.getAttachmentHome()+File.separator+relativePath;
    }
    private boolean saveFile(InputStream inputStream, String filePath) {
        File file = new File(filePath);
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取图片
     * @param module
     * @param fileName
     * @param response
     * @throws Exception
     */
    @RequestMapping("/image")
    public void getImage(String module, String fileName, HttpServletResponse response)
            throws Exception {
        if(StringUtils.isBlank(fileName)) return;
        try {
            String filePath = getFilePath(module, fileName);
            byte[] data = getAttachmentFile(filePath);

            response.setCharacterEncoding("utf-8");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
            response.flushBuffer();
        } catch(FileNotFoundException e) {
        } catch(IOException e) {
            log.error(e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
    private byte[] getAttachmentFile(String path) {
        byte[] fileArray = new byte[0];

        try {
            File file = new File(path);
            fileArray = FileUtils.readFileToByteArray(file);
        } catch(FileNotFoundException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileArray;
    }

}
