package com.sprint.mission.discodeit.storage.s3;

import static software.amazon.awssdk.core.sync.RequestBody.fromInputStream;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Component
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final String bucket;
  @Getter
  private final S3Client s3Client;
  private final S3Presigner presigner;
  @Value("${discodeit.storage.s3.presigned-url-expiration}")
  private long presignedUrlExpiration; // 초 단위 (기본값 600초 = 10분)

  S3BinaryContentStorage(
      @Value("${discodeit.storage.s3.access-key}") String accessKey,
      @Value("${discodeit.storage.s3.secret-key}") String secretKey,
      @Value("${discodeit.storage.s3.region}") String region,
      @Value("${discodeit.storage.s3.bucket}") String bucket) {
    this.bucket = bucket;

    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    this.s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    this.presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  @Override
  public UUID save(UUID binaryContentId, byte[] bytes) {
    String key = binaryContentId.toString();

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
      s3Client.putObject(putObjectRequest, fromInputStream(inputStream, bytes.length));
    } catch (IOException e) {
      throw new RuntimeException("Failed to upload to S3", e);
    }
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    String key = binaryContentId.toString();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    try {
      return s3Client.getObject(getObjectRequest);
    } catch (NoSuchKeyException e) {
      throw new NoSuchElementException("File with key " + key + " does not exist in S3");
    } catch (Exception e) {
      throw new RuntimeException("Failed to get object from S3", e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDTO metaData) {
    String key = metaData.id().toString();

    String presignedUrl = generatePresignedUrl(key, metaData.contentType());

    return ResponseEntity.status(HttpStatus.FOUND)
        .header(HttpHeaders.LOCATION, presignedUrl)
        .build();
  }

  public String generatePresignedUrl(String key, String contentType) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(contentType)
        .responseContentDisposition("attachment; filename=\"" + key + "\"")
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(presignedUrlExpiration))
        .getObjectRequest(getObjectRequest)
        .build();

    URL url = presigner.presignGetObject(presignRequest).url();
    return url.toString();
  }
}

