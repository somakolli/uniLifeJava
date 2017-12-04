package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.Models.Roles;
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

import java.util.List;

/**
 * Created by Sokol Makolli
 */
@RestController
@RequestMapping("/api/universities")
public class UniversityController {
    private final UniversityRepository universityRepository;
    private final UserRepository userRepository;

    @Autowired
    UniversityController(UniversityRepository universityRepository,
                         UserRepository userRepository){
        this.universityRepository = universityRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestBody University university){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userRepository.findByUsername(username).getRole().equals(Roles.Admin))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


        this.universityRepository.save(university);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<University>> getUniversities(){
        return ResponseEntity.ok(universityRepository.findAll());
    }

    @RequestMapping(method = RequestMethod.PUT)
    ResponseEntity<?> update(@RequestParam Long id, @RequestParam String name, @RequestParam String location){
        if(!universityRepository.exists(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("university not found");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userRepository.findByUsername(username).getRole().equals(Roles.Admin))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        University uni = universityRepository.findOne(id);
        uni.setName(name);
        uni.setLocation(location);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    @RequestMapping(method = RequestMethod.DELETE)
    ResponseEntity<?> delete(@RequestParam Long id){
        if(!universityRepository.exists(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("university not found");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userRepository.findByUsername(username).getRole().equals(Roles.Admin))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        universityRepository.delete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
