package com.android.kitob.controller;

import com.android.kitob.model.FileStorage;
import com.android.kitob.service.ApkBuilder;
import com.android.kitob.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

@RestController
@RequestMapping("api")
public class ApkController {

    @Autowired
    private ApkBuilder apkBuilder;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/build")
    public ResponseEntity build() throws IOException, InterruptedException, ScriptException {

        return ResponseEntity.ok(apkBuilder.build("a"));
    }

    /** File **/
    @GetMapping("/file/{name}")
    public HttpEntity<?> images(@PathVariable String name,
                                HttpServletRequest request) throws MalformedURLException {
        FileStorage fileStorage=fileStorageService.findByName(name);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline: filename\""+ URLEncoder.encode(fileStorage.getName()))
                .contentType(MediaType.parseMediaType(fileStorage.getContentType()))
                .contentLength(fileStorage.getFileSize())
                .body(new FileUrlResource(fileStorage.getUploadPath()));
    }

    /** Fayllar uchun **/

    @PostMapping("/upload")
    public HttpEntity<?> upload(@RequestParam String name,@RequestBody MultipartFile[] files){
//        String image=fileStorageService.save(multipartFile);
//        System.out.println(files.length);
        System.out.println(name);
        return ResponseEntity.ok(apkBuilder.file(files,name));
    }
}
