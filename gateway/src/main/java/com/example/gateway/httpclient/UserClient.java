package com.example.gateway.httpclient;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import com.example.gateway.dto.request.IntrospectRequest;
import com.example.gateway.dto.response.*;

import reactor.core.publisher.Mono;

// định nghĩa user client bằng HTTP Interfaces, chức năng tương tự như openfeign
// cấu hình ở webclient config, còn ở đây cũng khai báo các chức năng, api muốn gọi
public interface UserClient {
    @PostExchange(url  = "auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<APIResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);
}
 