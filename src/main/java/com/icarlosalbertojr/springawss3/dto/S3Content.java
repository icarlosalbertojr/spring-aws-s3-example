package com.icarlosalbertojr.springawss3.dto;

import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.services.s3.model.Owner;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.time.Instant;
import java.util.List;

@Builder
public record S3Content (
        String key,
        Instant lastModified,
        String eTag,
        List<String> checksumAlgorithm,
        Long size,
        String storageClass
) {
    public static S3Content of(S3Object s3Object) {
        return S3Content.builder()
                .checksumAlgorithm(s3Object.checksumAlgorithmAsStrings())
                .key(s3Object.key())
                .lastModified(s3Object.lastModified())
                .eTag(s3Object.eTag())
                .size(s3Object.size())
                .storageClass(s3Object.storageClassAsString())
                .build();
    }

}
