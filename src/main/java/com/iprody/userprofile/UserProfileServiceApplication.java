package com.iprody.userprofile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class, LiquibaseAutoConfiguration.class})
public class UserProfileServiceApplication {

    private static final Logger LOGGER = LogManager.getLogger(UserProfileServiceApplication.class);

    /**
     * @param args The command-line arguments passed to the program
     */
    public static void main(String[] args) {

        SpringApplication.run(UserProfileServiceApplication.class, args);

        
    }

}