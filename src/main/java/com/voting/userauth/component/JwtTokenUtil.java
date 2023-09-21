package com.voting.userauth.component;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.voting.userauth.model.User;
import com.voting.userauth.service.JwtSecretDecryptionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import software.amazon.awssdk.services.kms.KmsClient;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author michaelmak
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private Long expirationMs;

    @Value("${jwt.encryptedKey}")
    private String encryptedJwtSecretKey;

    private final AWSKMS kmsClient;

    @Autowired
    private final JwtSecretDecryptionService decryptionService;

    public JwtTokenUtil(JwtSecretDecryptionService decryptionService) {
        this.kmsClient = AWSKMSClientBuilder.standard().build();
        this.decryptionService = decryptionService;
    }

    public String generateToken(User user) {

        //insert an empty map
        Map<String, Object> claims = new HashMap<>();

        // Decrypt the JWT secret key
        String jwtSecretKey = decryptionService.decryptJwtSecretKey();

        // Generate the JWT token using the decrypted JWT secret key
        // Set the claims (payload) here
        // Use the decrypted key
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public boolean verifyToken(String token) {
        try {
            // Use the decrypted key

            String jwtSecretKey = decryptionService.decryptJwtSecretKey();
            Claims claims = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecretKey.getBytes()))
                    .parseClaimsJws(token)
                    .getBody();

            // If the parsing is successful, the token is valid.
            return true;
        } catch (SignatureException e) {
            // If the signature is invalid, the token is not valid.
            return false;
        }
    }
    // Extract username from a JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from a JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Validate if a JWT token is expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    // Extract a claim from a JWT token using the provided function
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from a JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
