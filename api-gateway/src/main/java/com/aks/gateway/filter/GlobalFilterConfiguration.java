package com.aks.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

// POST FILTER
//@Configuration
// Catch the HTTP-401 responses and refresh the token before responsing back
// Currently disabled
public class GlobalFilterConfiguration {
    @Autowired
    private TokenPreFilter tokenPreFilter;

    final Logger logger = LoggerFactory.getLogger(GlobalFilterConfiguration.class);

//    @Bean
    public GlobalFilter postGlobalFilter() {
        System.out.println("Filtering ... ");
        return (exchange, chain) -> {
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        logger.info("Global Post Filter executed");
                        if(exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED) {
                            tokenPreFilter.refreshToken(exchange.getRequest().getCookies().get("SESSION").get(0).getValue());
                        }
//                        else {
//                            tokenPreFilter.refreshToken(exchange.getRequest().getCookies().get("SESSION").get(0).getValue());
//                        }
                    }));
        };
    }
}
