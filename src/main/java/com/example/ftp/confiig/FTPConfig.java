package com.example.ftp.confiig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Configuration
@Validated
@ConfigurationProperties(prefix = "ftp")
public class FTPConfig {

    private String host;
    private String user;
    private int port;
    private String password;

}
