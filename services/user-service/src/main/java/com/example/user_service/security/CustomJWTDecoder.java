package com.example.user_service.security;

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

import com.example.user_service.dto.request.IntrospectRequest;
import com.example.user_service.service.AuthService;
import com.nimbusds.jose.JOSEException;

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

    AuthService authService;

    @NonFinal
    NimbusJwtDecoder jwtDecoder = null;

    @Override
    // decode token
    public Jwt decode(String token) throws JwtException {
        // check token  có valid hay k
        try{
            var response = authService.introspect(IntrospectRequest.builder()
                    .token(token)
                    .build());

            if(!response.isValid()) {
                throw new JwtException("Invalid token");
            }
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        // decode token dựa trên signer key
        if(Objects.isNull(jwtDecoder)) {
            byte[] decodedKey = signerKey.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "SHA256");

            jwtDecoder = NimbusJwtDecoder
                                    .withSecretKey(secretKeySpec)
                                    .macAlgorithm(MacAlgorithm.HS256)
                                    .build();
        }
        return jwtDecoder.decode(token);
    }
}
