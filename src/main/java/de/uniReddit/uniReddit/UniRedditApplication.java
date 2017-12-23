package de.uniReddit.uniReddit;

import com.oembedler.moon.graphql.boot.GraphQLWebAutoConfiguration;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import de.uniReddit.uniReddit.security.CloudJWT;
import graphql.Scalars;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
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
		try {
			return CloudJWT.getGoogleAuthToken();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

	}

}
