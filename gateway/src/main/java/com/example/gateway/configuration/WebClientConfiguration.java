package com.example.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.example.gateway.httpclient.UserClient;

@Configuration
// cấu hình web client theo HTTP Interfaces với ip cụ thể trước. Cái xử lý non-blocking giúp gateway k bị nghẽn cổ chai
public class WebClientConfiguration {

    @Bean
    WebClient webClient(){
        return WebClient.builder()
        .baseUrl("http://localhost:8081")
        .build();
    }
    
    @Bean
    UserClient userClient (WebClient webClient){
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
        .builderFor(WebClientAdapter.create(webClient))
        .build();

        return httpServiceProxyFactory.createClient(UserClient.class);
    }

    // nếu có nhiều client muốn config thì config như dưới để tránh xung đột bean

    // @Bean
    // UserClient userClient() {
    //     WebClient webClient = WebClient.builder()
    //             .baseUrl("http://localhost:8081")
    //             .build();
        
    //     return HttpServiceProxyFactory
    //             .builderFor(WebClientAdapter.create(webClient))
    //             .build()
    //             .createClient(UserClient.class);
    // }

    // @Bean
    // ProfileClient productClient() {
    //     WebClient webClient = WebClient.builder()
    //             .baseUrl("http://localhost:8082")
    //             .build();
        
    //     return HttpServiceProxyFactory
    //             .builderFor(WebClientAdapter.create(webClient))
    //             .build()
    //             .createClient(ProfileClient.class);
    // }

    // hoặc xây dựng hàm phụ để config cho tiện

    // private <T> T createClient(String url, Class<T> clazz) {
    //     WebClient webClient = WebClient.builder().baseUrl(url).build();
    //     return HttpServiceProxyFactory
    //             .builderFor(WebClientAdapter.create(webClient))
    //             .build()
    //             .createClient(clazz);
    // }

    // @Bean
    // UserClient userClient() {
    //     return createClient("http://localhost:8081", UserClient.class);
    // }

    // @Bean
    // ProfileClient productClient() {
    //     return createClient("http://localhost:8082", ProfileClient.class);
    // }
}
