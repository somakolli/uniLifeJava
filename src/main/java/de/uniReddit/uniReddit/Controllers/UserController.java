package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.GraphQL.HelperFunctions;
import de.uniReddit.uniReddit.Models.Roles;
import de.uniReddit.uniReddit.Models.UtUser;
import de.uniReddit.uniReddit.Models.University;
import de.uniReddit.uniReddit.Repositories.UniSubjectRepository;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @RequestMapping(method = RequestMethod.GET, value = "/{email}")
    UtUser get(@PathVariable String email){
        return userRepository.findByEmail(email);
    }

    /*
    @promise elevates the given user to the role admin if the currently authenticated user is an admin
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/elevate")
    ResponseEntity elevate(@RequestParam String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if(currentUsername==null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(!userRepository.findByEmail(currentUsername).getRole().equals(Roles.Admin))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UtUser UTUser = userRepository.findByEmail(username);
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
        UtUser UTUser = userRepository.findByEmail(username);
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
            if (userRepository.existsByEmail(value)) {
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

    @RequestMapping(method = RequestMethod.POST, value = "/setUniversity/{universityId}")
    ResponseEntity<?> setUniversity(@PathVariable Long universityId){
        UtUser user = HelperFunctions.getUser(userRepository);
        University university = HelperFunctions.checkExistance(universityRepository, universityId);
        user.setUniversity(university);
        userRepository.save(user);
        return ResponseEntity.accepted().build();
    }

}
