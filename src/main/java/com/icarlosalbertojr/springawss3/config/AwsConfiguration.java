package com.icarlosalbertojr.springawss3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class AwsConfiguration {

    
    private String awsAccessKey;
    private String awsSecretKey;
    private String awsS3Region;
    private String awsS3EndpointUrl;


    public AwsCredentials credentials() {
        return AwsBasicCredentials.create(awsAccessKey, awsSecretKey);
    }

    @Bean
    public S3Client amazonS3() {

        S3Configuration s3Configuration = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();

        return S3Client.builder()
                .region(Region.of(awsS3Region))
                .serviceConfiguration(s3Configuration)
                .endpointOverride(URI.create(awsS3EndpointUrl))
                .credentialsProvider(() -> credentials())
                .build();

    }

    @Autowired
    public void setAwsAccessKey(@Value("${aws.credentials.accessKey}") String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
    }

    @Autowired
    public void setAwsSecretKey(@Value("${aws.credentials.secretKey}") String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
    }

    @Autowired
    public void setAwsS3Region(@Value("${aws.s3.region}") String awsS3Region) {
        this.awsS3Region = awsS3Region;
    }

    @Autowired
    public void setAwsS3EndpointUrl(@Value("${aws.s3.endpointUrl}") String awsS3EndpointUrl) {
        this.awsS3EndpointUrl = awsS3EndpointUrl;
    }

    
}
