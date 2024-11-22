package com.acousea.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AcouseaBackendApplication {

    public static void main(String[] args) {
        springMain(args);
//        testMain(args);
    }


    public static void springMain(String[] args) {
        SpringApplication.run(AcouseaBackendApplication.class, args);
        System.out.println("Acousea backend application started!");
    }

    public static void testMain(String[] args) {

    }

}
