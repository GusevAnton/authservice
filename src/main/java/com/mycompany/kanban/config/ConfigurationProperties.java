package com.mycompany.kanban.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by antongusev on 11.11.16.
 */
@Component
@Data
public class ConfigurationProperties {

    @Value("${authentication.oauth.clientId}")
    private String clientId;

    @Value("${authentication.oauth.secret}")
    private String secretId;

    @Value("${authentication.oauth.tokenValidityInSeconds}")
    private int tokenValidityInSeconds;

    @Value("${address}")
    private String address;

}
