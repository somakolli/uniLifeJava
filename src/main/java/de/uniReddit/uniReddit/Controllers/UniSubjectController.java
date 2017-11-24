package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.Models.UniSubject;
import de.uniReddit.uniReddit.Models.UniSubjectBuilder;
import de.uniReddit.uniReddit.Models.University;
import de.uniReddit.uniReddit.Repositories.UniSubjectRepository;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        University university = universityRepository.findOne(universityId);
        this.uniSubjectRepository.save(UniSubjectBuilder.anUniSubject().name(name).university(university).build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @RequestMapping(method = RequestMethod.GET)
    List<UniSubject> getSubjects(@RequestParam Long universityId){
        return uniSubjectRepository.findAllByUniversityId(universityId);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/{uniSubjectId}")
    UniSubject getSubject(@PathVariable Long uniSubjectId){
        return uniSubjectRepository.findOne(uniSubjectId);
    }

}
