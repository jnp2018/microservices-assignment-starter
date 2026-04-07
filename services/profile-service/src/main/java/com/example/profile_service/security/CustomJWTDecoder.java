package com.example.profile_service.security;
import java.text.ParseException;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomJWTDecoder implements JwtDecoder {

    @NonFinal
    @Value("${jwt.signer}")
    private String signerKey;


    @NonFinal
    NimbusJwtDecoder jwtDecoder = null;

    // định nghĩa decoder cho token
    @Override
    public Jwt decode(String token) throws JwtException {

        if(Objects.isNull(jwtDecoder)) {
            byte[] decodedKey = signerKey.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "HmacSHA256");

            jwtDecoder = NimbusJwtDecoder
                                    .withSecretKey(secretKeySpec)
                                    .macAlgorithm(MacAlgorithm.HS256)
                                    .build();
        }
        return jwtDecoder.decode(token);
    }
}
