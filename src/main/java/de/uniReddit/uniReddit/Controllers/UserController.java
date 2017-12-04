package de.uniReddit.uniReddit.Controllers;

import com.fasterxml.jackson.annotation.JsonView;
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

    /*
    creates a user given the user encoded in json
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sign-up")
    ResponseEntity<?> add(@RequestBody UTUser UTUser) throws URISyntaxException {

        UTUser.setUniversity(universityRepository.findOne(UTUser.getUniversityId()));

        if(userRepository.existsByUsername(UTUser.getUsername()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("username exists");

        if(userRepository.existsByEmail(UTUser.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("email exists");

        if(!universityRepository.exists(UTUser.getUniversity().getId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("university not found");
        }
        University university = this.universityRepository.findOne(UTUser.getUniversityId());

        String passwordEnc = bCryptPasswordEncoder.encode(UTUser.getPassword());

        UTUser.setPassword(passwordEnc);

        this.userRepository.save(UTUser);

        URI uri = new URI("/api/users/" + UTUser.getUsername());
        return ResponseEntity.created(uri).build();

    }

    /*
    returns the given user given the pathvariable username
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    @JsonView(View.Everyone.class)
    UTUser get(@PathVariable String username){
        return userRepository.findByUsername(username);
    }

    /*
    subscribes the current user to the given subject
     */
    @RequestMapping(method = RequestMethod.GET, value = "/subscribe")
    ResponseEntity<?> subscribe(@RequestParam Long uniSubjectId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(!uniSubjectRepository.existsById(uniSubjectId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("uniSubject not found");
        UTUser UTUser = userRepository.findByUsername(username);
        UniSubject uniSubject = uniSubjectRepository.findOne(uniSubjectId);
        UTUser.subscribe(uniSubject);
        userRepository.save(UTUser);
        uniSubjectRepository.save(uniSubject);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /*
    @promise unbscribes the current user to the given subject
     */
    @RequestMapping(method = RequestMethod.GET, value = "/unsubscribe")
    ResponseEntity<?> unsubscribe( @RequestParam Long uniSubjectId){
        if(!uniSubjectRepository.existsById(uniSubjectId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("uniSubject not found");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UTUser UTUser = userRepository.findByUsername(username);
        UniSubject uniSubject = uniSubjectRepository.findOne(uniSubjectId);
        UTUser.unSubscribe(uniSubject);
        userRepository.save(UTUser);
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
        UTUser UTUser = userRepository.findByUsername(username);

        UTUser.setEmail(email);
        UTUser.setUniversity(universityRepository.findOne(universityId));
        UTUser.setEmail(email);
        this.userRepository.save(UTUser);
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

    /*
    @promise elevates the given user to the role admin if the currently authenticated user is an admin
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/elevate")
    ResponseEntity elevate(@RequestParam String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if(!userRepository.findByUsername(currentUsername).getRole().equals(Roles.Admin))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UTUser UTUser = userRepository.findByUsername(username);
        UTUser.setRole(Roles.Admin);
        userRepository.save(UTUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /*
    @return the role of the currently authenticated user
     */
    @RequestMapping(method = RequestMethod.GET, value = "/role")
    ResponseEntity<?> getRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UTUser UTUser = userRepository.findByUsername(username);
        return ResponseEntity.ok(UTUser.getRole());
    }

    /*
    @return the currently authenticated user
     */
    @JsonView(View.Authorized.class)
    @RequestMapping(method = RequestMethod.GET, value = "/me")
    ResponseEntity<UTUser> getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UTUser UTUser = userRepository.findByUsername(username);
        return ResponseEntity.ok(UTUser);
    }

    /*
    validates the property with the given value
    @return if value valid returns 200
    @return if value invalid returns http error with Error-Message header
     */
    @RequestMapping(method = RequestMethod.GET, value = "/validate/{property}/{value}")
    ResponseEntity<?> exists(@PathVariable String property,@PathVariable String value){
        value = value.replace("(dot)", ".");
        if(property.equals("username")) {
            if (userRepository.existsByUsername(value)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).header("Error-Message", "Username already in use.").build();
            }
            return ResponseEntity.ok().build();
        }
        if(property.equals("email")) {
            if (userRepository.existsByEmail(value)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).header("Error-Message", "Email already in use.").build();
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
