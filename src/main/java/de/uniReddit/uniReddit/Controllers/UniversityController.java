package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.Models.University;
import de.uniReddit.uniReddit.Repositories.UniSubjectRepository;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Sokol Makolli
 */
@RestController
@RequestMapping("/api/universities")
public class UniversityController {
    private final UniversityRepository universityRepository;

    @Autowired
    UniversityController(UniversityRepository universityRepository){
        this.universityRepository = universityRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestParam String name, @RequestParam String location){
        this.universityRepository.save(new University.UniversityBuilder().name(name).location(location).build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @RequestMapping(method = RequestMethod.GET)
    List<University> getUniversities(){
        return universityRepository.findAll();
    }
    @RequestMapping(method = RequestMethod.PUT)
    ResponseEntity<?> update(@RequestParam Long id, @RequestParam String name, @RequestParam String location){
        if(!universityRepository.exists(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("university not found");
        University uni = universityRepository.findOne(id);
        uni.setName(name);
        uni.setLocation(location);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    @RequestMapping(method = RequestMethod.DELETE)
    ResponseEntity<?> delete(@RequestParam Long id){
        if(!universityRepository.exists(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("university not found");
        universityRepository.delete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
