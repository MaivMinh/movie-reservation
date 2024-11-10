package com.foolish.moviereservation.service;

import com.azure.core.management.Resource;
import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AzureBlobService {
  private final Environment env;

  private String generateUniqueBlobName(String fileName) {
    String extension = "";
    int dotIndex = fileName.lastIndexOf(".");
    if (dotIndex != -1) {
      extension = fileName.substring(dotIndex);
      fileName = fileName.substring(0, dotIndex);
    }

    // Tạo tên blob với UUID và phần mở rộng
    return fileName + "_" + UUID.randomUUID() + extension;
  }

  public ByteArrayResource readBlobFile(String filename) {
    BlobClient blobClient = new BlobClientBuilder()
            .connectionString(env.getProperty("AZURE_STORAGE_CONNECTION_STRING"))
            .containerName("posters")
            .blobName(filename)
            .buildClient();

    if (!blobClient.exists()) {
      return null;
    }
    BinaryData data = blobClient.downloadContent();
    return new ByteArrayResource(data.toBytes());
  }

  public String writeBlobFile(MultipartFile file) {
    BlobClient blobClient = new BlobClientBuilder()
            .connectionString(env.getProperty("AZURE_STORAGE_CONNECTION_STRING"))
            .containerName("posters")
            .blobName(generateUniqueBlobName(file.getName()))
            .buildClient();
    try {
      blobClient.upload(file.getInputStream(), file.getBytes().length, false);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      return null;
    }
    return blobClient.getBlobName();
  }
}
