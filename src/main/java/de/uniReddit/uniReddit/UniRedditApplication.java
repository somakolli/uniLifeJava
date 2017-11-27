package de.uniReddit.uniReddit;

import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
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
		return "Still surviving.";
	}
}
