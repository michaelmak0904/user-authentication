package com.voting.userauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author michaelmak
 */
@Service
public class JwtService {

    @Autowired
    private final JwtSecretDecryptionService decryptionService;

    @Autowired
    public JwtService(JwtSecretDecryptionService decryptionService) {
        this.decryptionService = decryptionService;
    }

    public Claims verifyToken(String token) {
        String jwtSecretKey = decryptionService.decryptJwtSecretKey();

        // Verify the token using the decrypted JWT secret key
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }
}
