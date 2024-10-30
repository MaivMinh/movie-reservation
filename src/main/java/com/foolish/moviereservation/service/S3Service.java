package com.foolish.moviereservation.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

@Service
public class S3Service {

  private final S3Client s3Client;
  private final String bucketName;

  public S3Service(Environment environment, S3Client s3Client) {
    this.bucketName = environment.getProperty("bucketName");
    this.s3Client = s3Client;
  }

  public String uploadFile(MultipartFile file) throws IOException {
    String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(uniqueFileName)
            .build();

    PutObjectResponse response = s3Client.putObject(putObjectRequest,
            RequestBody.fromByteBuffer(ByteBuffer.wrap(file.getBytes())));
    return uniqueFileName;
  }
}
