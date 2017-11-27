package de.uniReddit.uniReddit.Controllers;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Sokol on 28.09.2017.
 */
@RestController
@RequestMapping("/api/unisubjects")
public class UniSubjectController {
    private final UserRepository userRepository;
    private final UniSubjectRepository uniSubjectRepository;
    private final UniversityRepository universityRepository;

    @Autowired
    UniSubjectController(UserRepository userRepository,
                   UniSubjectRepository uniSubjectRepository,
                   UniversityRepository universityRepository){
        this.userRepository = userRepository;
        this.uniSubjectRepository = uniSubjectRepository;
        this.universityRepository = universityRepository;
    }
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestParam String name, @RequestParam Long universityId){
        if (!universityRepository.exists(universityId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("university not found");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        University university = universityRepository.findOne(universityId);
        if(!user.getRole().equals(Roles.Admin)&&!user.getUniversityId().equals(universityId))
            ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        this.uniSubjectRepository.save(UniSubjectBuilder.anUniSubject().name(name).university(university).build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<UniSubject>> getSubjects(@RequestParam Long universityId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if(!user.getUniversityId().equals(universityId)&&!user.getRole().equals(Roles.Admin))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(uniSubjectRepository.findAllByUniversityId(universityId));
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{uniSubjectId}")
    ResponseEntity<UniSubject> getSubject(@PathVariable Long uniSubjectId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        UniSubject uniSubject = uniSubjectRepository.findOne(uniSubjectId);
        if(!user.getUniversityId().equals(uniSubject.getUniversity().getId())&&!user.getRole().equals(Roles.Admin))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(uniSubjectRepository.findOne(uniSubjectId));
    }

}
