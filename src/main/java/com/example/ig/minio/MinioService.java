package com.example.ig.minio;

import com.example.ig.entity.FileDto;
import io.minio.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.ig.IgApplication.user;

@Slf4j
@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public List<FileDto> getListObjects() {
        List<FileDto> objects = new ArrayList<>();
        try {
            Iterable<Result<Item>> result = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .recursive(true)
                    .build());
            for (Result<Item> item : result) {
                objects.add(FileDto.builder()
                        .filename(item.get().objectName())
                        .size(item.get().size())
                        .url(getPreSignedUrl(item.get().objectName()))
                        .build());
            }
            return objects;
        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
        }

        return objects;
    }

    private String getPreSignedUrl(String folderAndFilename) {
        return "http://localhost:8080/file/".concat(folderAndFilename);
    }

    public FileDto uploadFile(FileDto request, String folder, String name) {
        System.out.println(request);
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(name + ".jpg")
                    .stream(request.getFile().getInputStream(), request.getFile().getSize(), -1)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when upload file: ", e);
        }
        return FileDto.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .size(request.getFile().getSize())
                .url(getPreSignedUrl(folder + "/" + request.getFile().getOriginalFilename()))
                .filename(request.getFile().getOriginalFilename())
                .build();
    }

    public InputStream getObject(String filename, String folder) {
        InputStream stream;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
            return null;
        }

        return stream;
    }

}