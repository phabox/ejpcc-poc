package de.westlotto.draws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class DrawsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrawsApplication.class, args);
	}

}
