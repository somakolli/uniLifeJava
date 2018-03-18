package de.uniReddit.uniReddit;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import de.uniReddit.uniReddit.security.CloudJWT;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.servlet.GraphQLErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class UniRedditApplication {


    public static void main(String[] args) throws IOException {
        SpringApplication.run(UniRedditApplication.class, args);
        FileInputStream serviceAccount = new FileInputStream("src/unitalq-f77ad-firebase-adminsdk-u7nf7-b00cd4fc93.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://unitalq-f77ad.firebaseio.com")
                .build();
        FirebaseApp.initializeApp(options);
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

    @Bean
    public GraphQLErrorHandler errorHandler() {
        return new GraphQLErrorHandler() {
            @Override
            public List<GraphQLError> processErrors(List<GraphQLError> errors) {
                List<GraphQLError> clientErrors = new ArrayList<>();
                for (GraphQLError error : errors) {
                    if (isClientError(error)) {
                        clientErrors.add(error);
                    }
                }
                List<GraphQLError> serverErrors = new ArrayList<>();
                for (GraphQLError error : errors) {
                    if (!isClientError(error)) {
                        GraphQLErrorAdapter graphQLErrorAdapter = new GraphQLErrorAdapter(error);
                        serverErrors.add(graphQLErrorAdapter);
                    }
                }
                List<GraphQLError> e = new ArrayList<>();
                e.addAll(clientErrors);
                e.addAll(serverErrors);
                return e;
            }

            protected boolean isClientError(GraphQLError error) {
                return !(error instanceof ExceptionWhileDataFetching || error instanceof Throwable);
            }
        };
    }


}
