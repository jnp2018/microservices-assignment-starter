package com.example.gateway.configuration;

import java.util.Collection;
import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import com.example.gateway.dto.response.APIResponse;
import com.example.gateway.dto.response.IntrospectResponse;
import com.example.gateway.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
// @Configuration
public class AuthFilter implements GlobalFilter, Ordered{

    // danh sách các public api được duyệt qua luôn ở gateway
    static List<String> PUBLIC_ENDPOINTS = List.of(
        "/api/v1/auth/login",
        "/api/v1/user/register",
        "/api/v1/user/actuator",   
        "/api/v1/profile/actuator",
        "/actuator/health"
    );

    UserService userService;
    ObjectMapper objectMapper;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    // exchange chứa data của giao dịch http, gồm request, resposse. attributes
    // chain là các bước kiểm tra mà request phải qua trước khi đi tới service
    // các bước: AuthFilter tới Loggingfilter rồi RateLimit rồi đến service
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // bước 1: đi vào Authfilter

        // lây api bằng cách lấy request từ exchange -> lấy uri có tham số -> trích lấy riêng path
        String path = exchange.getRequest().getURI().getPath();

        String cleanPath = path.trim();
        log.info("DEBUG_PATH: [{}]", cleanPath);

        // nếu là public api thì cho qua luôn authfilter
        for (String publicPath : PUBLIC_ENDPOINTS) {
        log.info("Comparing: [{}] with [{}]", cleanPath, publicPath);
        if (cleanPath.startsWith(publicPath.trim())) {
            log.info("MATCH FOUND! Bypassing Auth for: {}", cleanPath);
            return chain.filter(exchange);
        }
    }
        // if (PUBLIC_ENDPOINTS.contains(path)) {
        //     return chain.filter(exchange); 
        // }

        // không public thì auth với jwt
        // lấy  authheader
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        // nếu ko có authheader thì chưa auth
        if(CollectionUtils.isEmpty(authHeader)){
            return unauthenticated(exchange.getResponse());
        }

        // đã có auth header thì lấy bearer token ra
        String token = authHeader.getFirst().replace("Bearer ", ""); 
        log.info("Token: {}", token);

        // kiểm tra token
        return userService.introspect(token).flatMap(introspectResponse -> {
            // nếu token valid (lombok để isValid vì biến valid là boolean nên ko phải getValid)
            if (introspectResponse.getData().isValid()) {
                // cho qua
                return chain.filter(exchange); 
            } else {
                // chưa auth
                return unauthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> {
            // nếu có lỗi thì in ra lỗi ở log và chưa auth
            log.error("Introspect failed: {}", throwable.getMessage());
            return unauthenticated(exchange.getResponse());
        });
    }

    private Mono<Void> unauthenticated(ServerHttpResponse response){
        // tạo response chứa lỗi unauth để trả về đúng lỗi khi auth không thành công
        APIResponse<?> apiResponse = APIResponse.builder().code(401).message("Chưa xác minh người dùng").build();
        String body = null;
        try {
            // đổi response sang dạng json string
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // set code 401 cho HTTP Status trả về
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // định nghĩa trả về dạng json và viết string đã map vào response
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
    
}
