package com.example.springbootconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// Configuration properties annotation tutorial

@Configuration
@ConfigurationProperties(prefix = "db")
public class DBSettings {
    private String connection;
    private String host;
    private int port;

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "DBSettings [connection=" + connection + ", host=" + host + ", port=" + port + "]";
    }

}
