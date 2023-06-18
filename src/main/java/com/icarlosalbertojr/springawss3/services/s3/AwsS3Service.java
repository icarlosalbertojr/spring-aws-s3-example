package com.icarlosalbertojr.springawss3.services.s3;

import com.icarlosalbertojr.springawss3.dto.S3Content;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AwsS3Service {

    private static final List<String> ALLOWED_FILE_EXTENSIONS = List.of("jpg", "png");
    private final S3Client s3Client;

    @Autowired
    public AwsS3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void createNewBucket(String bucketName) {
        try {
            var bucket = getBucket(bucketName);
            if (Objects.nonNull(bucket)) {
                throw new IllegalArgumentException("Bucket already exists");
            }
            s3Client.createBucket(CreateBucketRequest.builder()
                            .bucket(bucketName)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GetBucketAclResponse getBucket(String bucketName) {
        try {
            return s3Client.getBucketAcl(GetBucketAclRequest.builder()
                    .bucket(bucketName)
                    .build());
        } catch (NoSuchBucketException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBucket(String bucketName) {
        try {
            var bucket = getBucket(bucketName);

            if (Objects.isNull(bucket)) {
                throw new IllegalArgumentException("Bucket not exists");
            }

            s3Client.deleteBucket(DeleteBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            throw e;
        }
    }


    public void putObject(String bucketName, MultipartFile file) {

        if(!StringUtils.hasText(bucketName)) {
            throw new IllegalArgumentException("Bucket name is empty");
        }

        if(file.isEmpty()) {
            throw new IllegalArgumentException("File is missing");
        }

        var bucket = getBucket(bucketName);

        if (Objects.isNull(bucket)) {
            throw new RuntimeException("Bucket not exists");
        }

        var fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        if(!ALLOWED_FILE_EXTENSIONS.contains(fileExtension)) {
            throw new IllegalArgumentException("File is not allowed");
        }

        var newFile = new File(file.getOriginalFilename());
        try (final var fos = new FileOutputStream(newFile)) {
            fos.write(file.getBytes());
            fos.close();
            PutObjectRequest putObjectRequest = buildPutObject(bucketName, file);
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(newFile));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static PutObjectRequest buildPutObject(String bucketName, MultipartFile file) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getOriginalFilename())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();
    }


    public List<S3Content> listObjects(String bucketName) {
        var objectListing = s3Client.listObjects(ListObjectsRequest.builder()
                .bucket(bucketName)
                .build());
        return objectListing.contents()
                .stream()
                .map(S3Content::of)
                .toList();
    }

    public void deleteObject(String bucketName, String objectName){
        s3Client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectName)
                .build());
    }



    
}
