/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.domibus.webadmin.runner;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author spindlest
 */
@Configuration
@ConfigurationProperties(prefix="connector.database")
public class DatabaseConnectionContext {
    
    private final static Logger Log = LoggerFactory.getLogger(DatabaseConnectionContext.class);
    
    @Value("url")
    private String url;
    
    @Value("username")
    private String username;
    
    @Value("password")
    private String password;
    
    @Value("driverClassName")
    private String driverClassName;
    
    
    @Bean    
    public DataSource dataSource() {
        System.out.println("configure database with " + url);
        return DataSourceBuilder.create()
                .type(DataSourceBuilder.create().findType())
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
    }
    
    
}
