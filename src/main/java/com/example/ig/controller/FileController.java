package com.example.ig.controller;

import com.example.ig.entity.FileDto;
import com.example.ig.minio.MinioService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.example.ig.IgApplication.*;
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

    @RequestMapping(value = "/upload/post", method = RequestMethod.POST)
    public String uploadPost(@RequestParam("files") MultipartFile[] files) {
        for(MultipartFile file : files) {
            FileDto fileDto = FileDto.builder()
                    .file(file).build();
           minioService.uploadFile(fileDto, "Post", "post" + post.getId() + "_" + count);
            count++;
        }
        return "redirect:" + UserController.getUrl("group/" + group.getId() + "?");
    }

    @RequestMapping(value = "/rate", method = RequestMethod.POST)
    public String rateHandler(HttpServletRequest request) {
        //your controller code
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @GetMapping(value = "/post/**")
    public ResponseEntity<Object> getFileFromPost(HttpServletRequest request) throws IOException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(IOUtils.toByteArray(minioService.getObject(filename, "Post")));
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
