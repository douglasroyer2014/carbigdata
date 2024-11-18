package br.com.carbigdata.minio.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioBucketService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public MinioBucketService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostConstruct
    public void createBucketIfNotExists() {
        try {
            // Verifica se o bucket já existe
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!exists) {
                // Cria o bucket
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
                System.out.println("Bucket '" + bucketName + "' criado com sucesso!");
            } else {
                System.out.println("O bucket '" + bucketName + "' já existe.");
            }
        } catch (MinioException e) {
            System.out.println("Erro ao criar o bucket: " + e.getMessage());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
