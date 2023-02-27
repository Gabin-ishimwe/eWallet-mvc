package com.ewallet.apiGateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config>{

//    @Autowired
//    WebClient.Builder webClient;

    AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing auth information");
            }
            // get token from header
            String authToken = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
            // split token
            String[] parts = authToken.split(" ");
            // check if token is valid
            if(parts.length != 2 || !"Bearer".equals(parts[0])) {
                throw new RuntimeException("Incorrect Token structure");
            }
            // customizing headers when endpoint is protected
            // testing out dummy logic

            return chain.filter(exchange.mutate().request(exchange.getRequest().mutate().header("oauth", String.valueOf(true)).build()).build());
        });
    }


//    public static class Config {}
}
