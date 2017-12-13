package de.uniReddit.uniReddit.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniReddit.uniReddit.Models.UTUser;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import de.uniReddit.uniReddit.security.CloudJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.HeaderParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/data")
public class Data {

    private final UserRepository userRepository;

    @Autowired
    public Data(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/uploadProfilePicture")
    ResponseEntity<String> uploadProfilePicture(@RequestParam String contentType,
                                           @RequestParam String contentLength,
                                           @RequestParam String name){
        final String uri = "https://www.googleapis.com/upload/storage/v1/b/uni-talq-datastore/o?uploadType=resumable&name="+name;
        name = UUID.randomUUID() + name;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Authorization", "Bearer " + CloudJWT.getGoogleAuthToken());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        headers.add("X-Upload-Content-Type", contentType);
        headers.add("X-Upload-Content-Length", contentLength);

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        try{
        ResponseEntity responseEntity =    restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UTUser UTUser = userRepository.findByUsername(username);
            UTUser.setProfilePictureUrl("https://www.googleapis.com/storage/v1/b/uni-talq-datastore/o/." + UTUser.getId() + "." + name);
            ObjectMapper objectMapper = new ObjectMapper();
        return ResponseEntity.status(200).body(objectMapper.writeValueAsString(responseEntity.getHeaders().get("Location").get(0)));

        }catch (HttpClientErrorException e){
           return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
