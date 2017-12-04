package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.UniSubjectRepository;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/api/uniSubjects")
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
    ResponseEntity<?> add(@RequestBody UniSubject uniSubject){
        Long universityId = uniSubject.getUniversityId();
        if (!universityRepository.exists(universityId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("university not found");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UTUser user = userRepository.findByUsername(username);

        University university = universityRepository.findOne(universityId);
        if(!user.getRole().equals(Roles.Admin)&&!user.getUniversityId().equals(universityId))
            ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        uniSubject.setUniversity(universityRepository.findOne(universityId));
        this.uniSubjectRepository.save(uniSubject);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<Page<UniSubject>> getSubjects(@RequestParam Long universityId,
                                                 @RequestParam int page,
                                                 @RequestParam int pageSize,
                                                 @RequestParam String sortDirection,
                                                 @RequestParam String sortProperties){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UTUser UTUser = userRepository.findByUsername(username);

        if(!UTUser.getUniversityId().equals(universityId)&&!UTUser.getRole().equals(Roles.Admin))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(uniSubjectRepository
                .findAllByUniversity(universityRepository.findOne(universityId),
                        new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                                sortProperties)));
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{uniSubjectId}")
    ResponseEntity<UniSubject> getSubject(@PathVariable Long uniSubjectId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UTUser UTUser = userRepository.findByUsername(username);

        UniSubject uniSubject = uniSubjectRepository.findOne(uniSubjectId);
        if(!UTUser.getUniversityId().equals(uniSubject.getUniversity().getId())&&!UTUser.getRole().equals(Roles.Admin))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(uniSubjectRepository.findOne(uniSubjectId));
    }

}
