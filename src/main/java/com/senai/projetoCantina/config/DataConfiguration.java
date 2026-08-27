package com.senai.projetoCantina.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataConfiguration {

    private static final String URL      = "jdbc:mysql://localhost:3306/cantina_final"
                                         + "?createDatabaseIfNotExist=true"
                                         + "&useTimezone=true"
                                         + "&serverTimezone=UTC"
                                         + "&useSSL=false"
                                         + "&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "senai@126";

    @Primary
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(URL);
        ds.setUsername(USERNAME);
        ds.setPassword(PASSWORD);
        return ds;
    }
}
