package de.uniReddit.uniReddit;

import de.uniReddit.uniReddit.security.CloudJWT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
@RestController
public class UniRedditApplication {


	public static void main(String[] args) {
		SpringApplication.run(UniRedditApplication.class, args);

	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@RequestMapping("/_ah/health")
	public String healthy() {
		// Message body required though ignored
		return "Still breathing";

	}

}
