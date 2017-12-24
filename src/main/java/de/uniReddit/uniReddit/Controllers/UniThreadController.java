package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Sokol on 14.11.2017.
 */
@RestController
@RequestMapping("api/uniThreads")
public class UniThreadController {
    private final UserRepository userRepository;
    private final UniSubjectRepository uniSubjectRepository;
    private final UniversityRepository universityRepository;
    private final ThreadRepository uniThreadRepository;

    @Autowired
    UniThreadController(UserRepository userRepository,
                        UniSubjectRepository uniSubjectRepository,
                        UniversityRepository universityRepository,
                        ThreadRepository threadRepository
    ){
        this.userRepository = userRepository;
        this.uniSubjectRepository = uniSubjectRepository;
        this.universityRepository = universityRepository;
        this.uniThreadRepository = threadRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestBody UniThread uniThread){
        if(!uniSubjectRepository.exists(uniThread.getUniSubjectId()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("subject not found");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UTUser author = userRepository.findByUsername(username);
        UniSubject uniSubject = uniSubjectRepository.findOne(uniThread.getUniSubjectId());
        uniThread.setUniSubject(uniSubject);
        uniThread.setCreator(author);
        uniThreadRepository.save(uniThread);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @RequestMapping(method = RequestMethod.GET, value = "/one")
    ResponseEntity<UniThread> getOne(@RequestParam Long threadId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser UTUser = userRepository.findByUsername(username);
        if(!uniThreadRepository.exists(threadId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        UniThread uniThread = uniThreadRepository.findOne(threadId);
        if(!UTUser.getRole().equals(Roles.Admin)
                &&!UTUser.getUniversityId().equals(uniThread.getUniversityId()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(uniThread);
    }
    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<UniThread>> getAll(@RequestParam Long uniSubjectId,
                                           @RequestParam int page,
                                           @RequestParam int pageSize,
                                           @RequestParam String sortDirection,
                                           @RequestParam String sortProperties){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser UTUser = userRepository.findByUsername(username);
        if(!UTUser.getRole().equals(Roles.Admin)
                &&!UTUser.getUniversityId().equals(uniSubjectRepository.findOne(uniSubjectId).getUniversity().getId()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UniSubject uniSubject = uniSubjectRepository.findOne(uniSubjectId);
        return ResponseEntity
                .ok(uniThreadRepository
                        .findAllByUniSubject(uniSubject,
                                new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                                        sortProperties)));
    }

}
