package com.example.user_service.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.dto.request.AuthRequest;
import com.example.user_service.dto.request.IntrospectRequest;
import com.example.user_service.dto.request.LogoutRequest;
import com.example.user_service.dto.response.AuthResponse;
import com.example.user_service.dto.response.IntrospectResponse;
import com.example.user_service.entity.InvalidToken;
import com.example.user_service.entity.User;
import com.example.user_service.enums.ErrorCode;
import com.example.user_service.exception.AppException;
import com.example.user_service.repository.InvalidTokenRepository;
import com.example.user_service.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    InvalidTokenRepository invalidTokenRepository;  

    @NonFinal
    @Value("${jwt.signer}")
    String signerKey;

    // check xem token có valid không (hàm introspect)
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException{
        var token = request.getToken();

        boolean isValid = true;
        
        try{
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    // logic verify token
    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {

        // khởi tạo bộ xác minh với signer key
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());

        // giải mã token từ String thành nội dung cụ thể
        SignedJWT signedJWT = SignedJWT.parse(token);

        // verify token
        var verified = signedJWT.verify(verifier);

        // lấy jwt ID
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
         
        // lấy hạn của token
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // mếu token hết hạn hoặc k pass vòng verify thì nhả lỗi 401
        if(!((Boolean)verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // token hết hạn thì cũng bị 401
        if (invalidTokenRepository.existsById(jwtId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    // dành cho login: check trùng lặp của username và hashed password nhập khi login với hashed password trong db
    @Transactional
    public AuthResponse auth(AuthRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean isAuth = passwordEncoder.matches(request.getPassword(), user.getPassword());


        if(!isAuth) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);

        return AuthResponse.builder()
        .token(token)
        .isAuth(isAuth)
        .build();
    }


    // logout: chỉ lưu token hết hạn 
    public void logout(LogoutRequest request) throws JOSEException, ParseException{
        var signToken = verifyToken(request.getToken());
        String jwtId = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidToken invalidToken = InvalidToken.builder()
                .id(jwtId)
                .expiryTime(expiryTime)
                .build();

        invalidTokenRepository.save(invalidToken);

    }

    // sinh token khi login
    private String generateToken(User user) {
        // header với thuật toán mã hóa token
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        // claimset chứa thông tin muốn cho vào token:
        // . subject là chính của token, lấy ra bằng .name(), thường để là username
        // .issueTime và exprirationTime là tgian bắt đầu và hết hạn
        // . jwtID: id của token
        // claim (key, value) là cặp giá trị muốn thêm
        // key mặc định scope là để chứa role, permission
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(user.getUsername())
        .issuer("demo.com")
        .issueTime(new Date())
        .expirationTime(new Date(
            Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
        .jwtID(UUID.randomUUID().toString())
        .claim("scope", buildScope(user))
        .claim("userId", user.getId())
        .build();
        
        // gán claimset vào payload
        Payload payload = new Payload(claimsSet.toJSONObject());
         
        // tạo jwt với header và payload
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            // biến object thành string 
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        // Build scope với prefix: ROLE_ cho roles, PERMISSION_ cho permissions
        // vì đã config prefix là "" ở file config nên ở đây gán thêm ROLE_, PERMISSION_ để author cho dễ 
        String rolesString = String.join(" ",
            user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName())
                .toList()
        );

        String permissionsString = String.join(" ",
            user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(perm -> "PERMISSION_" + perm.getName())
                .distinct()
                .toList()
        );

        return (rolesString + " " + permissionsString).trim();
    }
}
