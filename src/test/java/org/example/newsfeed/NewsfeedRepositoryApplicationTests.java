package org.example.newsfeed;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy
@SpringBootTest
class NewsfeedRepositoryApplicationTests {

    public static void main(String[] args) {
        SpringApplication.run(NewsfeedApplication.class, args);
  }

}
