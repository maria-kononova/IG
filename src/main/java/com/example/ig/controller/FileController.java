package com.example.ig.controller;

import com.example.ig.entity.FileDto;
import com.example.ig.minio.MinioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;

@Slf4j
@Controller
@RequestMapping(value = "/file")
public class FileController {

    @Autowired
    private MinioService minioService;

    @GetMapping
    public ResponseEntity<Object> getFiles() {
        return ResponseEntity.ok(minioService.getListObjects());
    }
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(FileDto request) {
        minioService.uploadFile(request);
        return "redirect:" + UserController.getUrl("account");
    }

    @RequestMapping(value = "/rate", method = RequestMethod.POST)
    public String rateHandler(HttpServletRequest request) {
        //your controller code
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @GetMapping(value = "/**")
    public ResponseEntity<Object> getFile(HttpServletRequest request) throws IOException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(IOUtils.toByteArray(minioService.getObject(filename)));
    }
}
