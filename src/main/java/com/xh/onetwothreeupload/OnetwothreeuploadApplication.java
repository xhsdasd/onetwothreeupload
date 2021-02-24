package com.xh.onetwothreeupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication

public class OnetwothreeuploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnetwothreeuploadApplication.class, args);
    }

}
