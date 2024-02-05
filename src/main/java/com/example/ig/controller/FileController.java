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

import static com.example.ig.IgApplication.group;
import static com.example.ig.IgApplication.user;
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
    @RequestMapping(value = "/upload/group", method = RequestMethod.POST)
    public String uploadGroup(FileDto request) {
        System.out.println(request);
        minioService.uploadFile(request, "Group", "group" + group.getId());
        return "redirect:" + UserController.getUrl("group/" + group.getId() + "?");
    }
    @RequestMapping(value = "/upload/avatar", method = RequestMethod.POST)
    public String uploadAvatar(FileDto request) {
        minioService.uploadFile(request, "Avatar", "avatar" + user.getId());
        return "redirect:" + UserController.getUrl("account");
    }

    @RequestMapping(value = "/rate", method = RequestMethod.POST)
    public String rateHandler(HttpServletRequest request) {
        //your controller code
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @GetMapping(value = "/avatar/**")
    public ResponseEntity<Object> getFileFromAvatar(HttpServletRequest request) throws IOException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(IOUtils.toByteArray(minioService.getObject(filename, "Avatar")));
    }
    @GetMapping(value = "/group/**")
    public ResponseEntity<Object> getFileFromGroup(HttpServletRequest request) throws IOException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(IOUtils.toByteArray(minioService.getObject(filename, "Group")));
    }
}
