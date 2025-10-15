package com.sprint.mission.discodeit.storage.s3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

class AWSS3Test {

  private static S3Client s3Client;
  private static S3Presigner s3Presigner;
  private static String bucketName;

  @BeforeAll
  static void setUp() throws IOException {
    Properties props = new Properties();

    // ✅ 1️⃣ .env 파일이 있으면 읽기
    File envFile = new File(".env");
    if (envFile.exists()) {
      props.load(new FileInputStream(envFile));
    }

    // ✅ 2️⃣ 환경 변수에서 읽기 (없으면 .env 값, 그것도 없으면 기본값)
    String accessKey = System.getenv().getOrDefault("AWS_S3_ACCESS_KEY",
        props.getProperty("AWS_S3_ACCESS_KEY", "default-access-key"));
    String secretKey = System.getenv().getOrDefault("AWS_S3_SECRET_KEY",
        props.getProperty("AWS_S3_SECRET_KEY", "default-secret-key"));
    String region = System.getenv().getOrDefault("AWS_REGION",
        props.getProperty("AWS_REGION", "ap-northeast-2"));
    bucketName = System.getenv().getOrDefault("AWS_S3_BUCKET",
        props.getProperty("AWS_S3_BUCKET", "default-bucket"));

    System.out.printf("Using AWS config: region=%s, bucket=%s%n", region, bucketName);

    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    s3Presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  @AfterEach
  void cleanUp() {
    String[] filesToDelete = {"storage/test-upload.txt", "storage/download-upload.txt"};

    for (String filePath : filesToDelete) {
      File file = new File(filePath);
      if (file.exists()) {
        boolean deleted = file.delete();
        if (!deleted) {
          System.err.println("Failed to delete file: " + filePath);
        }
      }
    }
  }

  @Test
  @DisplayName("업로드 테스트")
  void uploadTest() throws IOException {
    String key = "test-upload.txt";
    File file = new File("storage", key);

    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }

    if (!file.exists()) {
      file.createNewFile();
    }

    String s3Key = "test/" + key;
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(s3Key)
        .build();

    s3Client.putObject(putObjectRequest, Paths.get(file.getAbsolutePath()));

    System.out.println("upload success: " + key);
  }

  @Test
  @DisplayName("파일 읽어오기 테스트")
  void getFileTest() {
    String s3Key = "test/test-upload.txt";

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(s3Key)
        .build();

    System.out.println("read complete: " + getObjectRequest);
  }

  @Test
  @DisplayName("파일 다운로드 테스트")
  void downloadTest() {
    String s3Key = "test/test-upload.txt";
    File file = new File("storage/download-upload.txt");

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(s3Key)
        .build();

    s3Client.getObject(getObjectRequest, Paths.get(file.getAbsolutePath()));

    System.out.println("Download complete: " + file.getAbsolutePath());
  }

  @Test
  @DisplayName("임시 URL 생성 테스트")
  void createPresignedUrlTest() {
    String s3Key = "test/test-upload.txt";

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(s3Key)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .getObjectRequest(getObjectRequest)
        .build();

    URL presignedUrl = s3Presigner.presignGetObject(presignRequest).url();

    System.out.println("Presigned url: " + presignedUrl);
  }
}