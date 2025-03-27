package ru.alexandr.bankparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BankParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankParserApplication.class, args);
    }

}
