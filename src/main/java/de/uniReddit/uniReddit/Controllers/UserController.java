package de.uniReddit.uniReddit.Controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.sun.org.apache.regexp.internal.RE;
import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.UniSubjectRepository;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Created by Sokol on 25.09.2017.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final UniSubjectRepository uniSubjectRepository;
    private final UniversityRepository universityRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserController(UserRepository userRepository,
                   UniSubjectRepository uniSubjectRepository,
                   UniversityRepository universityRepository,
                   BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.uniSubjectRepository = uniSubjectRepository;
        this.universityRepository = universityRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sign-up")
    ResponseEntity<?> add(@RequestBody User user) throws URISyntaxException {

        user.setUniversity(universityRepository.findOne(user.getUniversityId()));

        if(userRepository.existsByUsername(user.getUsername()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("username exists");

        if(userRepository.existsByEmail(user.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("email exists");

        if(!universityRepository.exists(user.getUniversity().getId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("university not found");
        }
        University university = this.universityRepository.findOne(user.getUniversityId());

        String passwordEnc = bCryptPasswordEncoder.encode(user.getPassword());

        this.userRepository.save(user);

        URI uri = new URI("/api/users/" + user.getUsername());
        return ResponseEntity.created(uri).build();

    }
    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    @JsonView(View.Everyone.class)
    User get(@PathVariable String username){
        return userRepository.findByUsername(username);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/subscribe")
    ResponseEntity<?> subscribe(@RequestParam Long uniSubjectId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(!uniSubjectRepository.existsById(uniSubjectId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("uniSubject not found");
        User user = userRepository.findByUsername(username);
        UniSubject uniSubject = uniSubjectRepository.findOne(uniSubjectId);
        user.subscribe(uniSubject);
        userRepository.save(user);
        uniSubjectRepository.save(uniSubject);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    @RequestMapping(method = RequestMethod.GET, value = "/unsubscribe")
    ResponseEntity<?> unsubscribe( @RequestParam Long uniSubjectId){
        if(!uniSubjectRepository.existsById(uniSubjectId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("uniSubject not found");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        UniSubject uniSubject = uniSubjectRepository.findOne(uniSubjectId);
        user.unSubscribe(uniSubject);
        userRepository.save(user);
        uniSubjectRepository.save(uniSubject);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    @RequestMapping(method = RequestMethod.PUT)
    ResponseEntity<?> update(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam Long universityId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        if(!currentUsername.equals(username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!userRepository.existsByUsername(username))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("username not found");
        if(!universityRepository.exists(universityId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("universoty not found");
        User user = userRepository.findByUsername(username);

        user.setEmail(email);
        user.setUniversity(universityRepository.findOne(universityId));
        user.setEmail(email);
        this.userRepository.save(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    ResponseEntity<?> delete(@RequestParam String username){
        if(!userRepository.existsByUsername(username))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("username not found");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        if(!currentUsername.equals(username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        this.universityRepository.delete(userRepository.findByUsername(username).getId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/elevate")
    ResponseEntity elevate(@RequestParam String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if(!userRepository.findByUsername(currentUsername).getRole().equals(Roles.Admin))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = userRepository.findByUsername(username);
        user.setRole(Roles.Admin);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


    @RequestMapping(method = RequestMethod.GET, value = "/role")
    ResponseEntity<?> getRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        return ResponseEntity.ok(user.getRole());
    }

    @JsonView(View.Authorized.class)
    @RequestMapping(method = RequestMethod.GET, value = "/me")
    ResponseEntity<User> getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        return ResponseEntity.ok(user);
    }
}
