package com.icarlosalbertojr.springawss3.controllers;

import com.icarlosalbertojr.springawss3.dto.S3Content;
import com.icarlosalbertojr.springawss3.services.s3.AwsS3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/s3")
public class AwsS3Controller {
    
    private final AwsS3Service awsS3Service;

    public AwsS3Controller(AwsS3Service awsS3Service) {
        this.awsS3Service = awsS3Service;
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadFile(@RequestPart("file") MultipartFile file, @RequestParam String bucketName) {
        awsS3Service.putObject(bucketName, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/image")
    public List<S3Content> getFiles(@RequestParam String bucketName) {
        return awsS3Service.listObjects(bucketName);
    }

    @DeleteMapping("/image")
    public ResponseEntity<?> deleteFile(@RequestParam String bucketName, @RequestParam String fileName) {
        awsS3Service.deleteObject(bucketName, fileName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/bucket")
    public ResponseEntity<?> createBucket(@RequestParam String bucketName) {
        awsS3Service.createNewBucket(bucketName);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/bucket")
    public ResponseEntity<?> deleteBucket(@RequestParam String bucketName) {
        awsS3Service.deleteBucket(bucketName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
