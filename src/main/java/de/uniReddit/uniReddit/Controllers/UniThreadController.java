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

import java.util.Collection;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by Sokol on 14.11.2017.
 */
@RestController
@RequestMapping("api/unithreads")
public class UniThreadController {
    private final UserRepository userRepository;
    private final UniSubjectRepository uniSubjectRepository;
    private final UniversityRepository universityRepository;
    private final ThreadRepository uniThreadRepository;
    private final PostContentRepository postContentRepository;
    @Autowired
    UniThreadController(UserRepository userRepository,
                        UniSubjectRepository uniSubjectRepository,
                        UniversityRepository universityRepository,
                        ThreadRepository threadRepository,
                        PostContentRepository postContentRepository
    ){
        this.userRepository = userRepository;
        this.uniSubjectRepository = uniSubjectRepository;
        this.universityRepository = universityRepository;
        this.uniThreadRepository = threadRepository;
        this.postContentRepository = postContentRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestParam String title,
                          @RequestParam long subjectId,
                          @RequestParam String contentString){
        if(!uniSubjectRepository.exists(subjectId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("subject not found");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User author = userRepository.findByUsername(username);
        UniSubject uniSubject = uniSubjectRepository.findOne(subjectId);
        PostContent content = new PostContent.PostContentBuilder().content(contentString).build();
        postContentRepository.save(content);
        UniThread thread = new UniThread.UniThreadBuilder().content(content.getId())
                .creator(author).title(title).uniSubject(uniSubject).build();
        uniThreadRepository.save(thread);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @RequestMapping(method = RequestMethod.GET, value = "/one")
    ResponseEntity<UniThread> getOne(@RequestParam Long threadId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if(!uniThreadRepository.exists(threadId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        UniThread uniThread = uniThreadRepository.findOne(threadId);
        if(!user.getRole().equals(Roles.Admin)
                &&!user.getUniversityId().equals(uniThread.getUniversityId()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(uniThread);
    }
    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<Page<UniThread>> getAll(@RequestParam Long subjectId,
                                           @RequestParam int page,
                                           @RequestParam int pageSize,
                                           @RequestParam String sortDirection,
                                           @RequestParam String sortProperties){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if(!user.getRole().equals(Roles.Admin)
                &&!user.getUniversityId().equals(uniSubjectRepository.findOne(subjectId).getUniversity().getId()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UniSubject uniSubject = uniSubjectRepository.findOne(subjectId);
        return ResponseEntity
                .ok(uniThreadRepository
                        .findAllByUniSubject(uniSubject,
                                new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                                        sortProperties)));
    }

}
