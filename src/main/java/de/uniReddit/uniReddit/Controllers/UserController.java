package de.uniReddit.uniReddit.Controllers;

import com.fasterxml.jackson.annotation.JsonView;
import de.uniReddit.uniReddit.Models.Roles;
import de.uniReddit.uniReddit.Models.UTUser;
import de.uniReddit.uniReddit.Models.UniSubject;
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


/**
 * Created by Sokol on 25.09.2017.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final UniSubjectRepository uniSubjectRepository;
    private final UniversityRepository universityRepository;

    @Autowired
    UserController(UserRepository userRepository,
                   UniSubjectRepository uniSubjectRepository,
                   UniversityRepository universityRepository){
        this.userRepository = userRepository;
        this.uniSubjectRepository = uniSubjectRepository;
        this.universityRepository = universityRepository;
    }

    /*
    returns the given user given the pathvariable username
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    UTUser get(@PathVariable String username){
        return userRepository.findByUsername(username);
    }

    /*
    subscribes the current user to the given subject
     */
    @RequestMapping(method = RequestMethod.POST, value = "/subscribe")
    ResponseEntity<?> subscribe(@RequestParam Long uniSubjectId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username=="anonymousUser") return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!uniSubjectRepository.exists(uniSubjectId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("uniSubject not found");
        UTUser UTUser = userRepository.findByUsername(username);
        UniSubject uniSubject = uniSubjectRepository.findOne(uniSubjectId);
        UTUser.subscribe(uniSubject);
        userRepository.save(UTUser);
        uniSubjectRepository.save(uniSubject);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /*
    @promise unsubcribes the current user to the given subject
     */
    @RequestMapping(method = RequestMethod.POST, value = "/unsubscribe")
    ResponseEntity<?> unsubscribe( @RequestParam Long uniSubjectId){
        if(!uniSubjectRepository.exists(uniSubjectId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("uniSubject not found");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username==null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
        if(username==null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
        if(currentUsername==null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
        if(username==null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UTUser UTUser = userRepository.findByUsername(username);
        return ResponseEntity.ok(UTUser.getRole());
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
