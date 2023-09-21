package com.voting.userauth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;

/**
 * @author michaelmak
 */
@Component
public class JwtSecretDecryptionService {

    // Load the encrypted key from application.properties

    @Value("${jwt.encryptedKey}")
    private String encryptedJwtSecretKey;

    private final KmsClient kmsClient;

    public JwtSecretDecryptionService(KmsClient kmsClient) {
        this.kmsClient = kmsClient;
    }

    public String decryptJwtSecretKey() {
        DecryptRequest decryptRequest = DecryptRequest.builder()
                .ciphertextBlob(SdkBytes.fromByteBuffer(java.nio.ByteBuffer.wrap(java.util.Base64.getDecoder().decode(encryptedJwtSecretKey))))
                .build();

        DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);
        byte[] plaintext = decryptResponse.plaintext().asByteArray();

        return new String(plaintext);
    }
}
