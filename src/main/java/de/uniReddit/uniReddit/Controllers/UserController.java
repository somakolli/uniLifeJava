package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.Models.UniSubject;
import de.uniReddit.uniReddit.Models.University;
import de.uniReddit.uniReddit.Models.User;
import de.uniReddit.uniReddit.Repositories.UniSubjectRepository;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    UserController(UserRepository userRepository,
                   UniSubjectRepository uniSubjectRepository,
                   UniversityRepository universityRepository){
        this.userRepository = userRepository;
        this.uniSubjectRepository = uniSubjectRepository;
        this.universityRepository = universityRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestParam String username,
                          @RequestParam String email,
                          @RequestParam Long universityId) throws URISyntaxException {

        if(userRepository.existsByUsername(username))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("username exists");

        if(userRepository.existsByEmail(email))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("email exists");

        if(!universityRepository.exists(universityId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("university not found");
        }
        University university = this.universityRepository.findOne(universityId);

        this.userRepository.save(new User.UserBuilder().username(username).email(email).university(university).build());
        URI uri = new URI("/api/users/" + username);
        return ResponseEntity.created(uri).build();

    }
    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    User get(@PathVariable String username){
        return userRepository.findByUsername(username);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/subscribe")
    ResponseEntity<?> subscribe(@RequestParam String username, @RequestParam Long uniSubjectId){
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
    ResponseEntity<?> unsubscribe(@RequestParam String username, @RequestParam Long uniSubjectId){
        if(!uniSubjectRepository.existsById(uniSubjectId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("uniSubject not found");
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
        this.universityRepository.delete(userRepository.findByUsername(username).getId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


}
