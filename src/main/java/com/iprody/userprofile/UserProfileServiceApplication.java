package com.iprody.userprofile;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;


@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class, LiquibaseAutoConfiguration.class})
@Slf4j
@RequiredArgsConstructor
public class UserProfileServiceApplication {

    private static final Logger LOGGER = LogManager.getLogger(UserProfileServiceApplication.class);

    private final Tracer tracer;

    /**
     * @param args The command-line arguments passed to the program
     */
    public static void main(String[] args) {

        SpringApplication.run(UserProfileServiceApplication.class, args);
    }
}