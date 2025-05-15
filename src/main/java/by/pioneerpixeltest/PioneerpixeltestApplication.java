package by.pioneerpixeltest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PioneerpixeltestApplication {

    public static void main(String[] args) {
        SpringApplication.run(PioneerpixeltestApplication.class, args);
    }

}
