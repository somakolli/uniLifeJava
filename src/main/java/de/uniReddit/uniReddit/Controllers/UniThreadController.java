package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.Models.PostContent;
import de.uniReddit.uniReddit.Models.UniSubject;
import de.uniReddit.uniReddit.Models.UniThread;
import de.uniReddit.uniReddit.Models.User;
import de.uniReddit.uniReddit.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<?> add(@RequestParam String title, @RequestParam long subjectId, @RequestParam String contentString, @RequestParam long authorId){
        if(!uniSubjectRepository.exists(subjectId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("subject not found");
        User author = userRepository.findOne(authorId);
        UniSubject uniSubject = uniSubjectRepository.findOne(subjectId);
        PostContent content = new PostContent.PostContentBuilder().content(contentString).build();
        postContentRepository.save(content);
        UniThread thread = new UniThread.UniThreadBuilder().content(content.getId()).creator(author).title(title).uniSubject(uniSubject).build();
        uniThreadRepository.save(thread);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @RequestMapping(method = RequestMethod.GET)
    Collection<UniThread> getAll(@RequestParam Long subjectId){
        return uniThreadRepository.findAllByUniSubject(uniSubjectRepository.findOne(subjectId));
    }
    @RequestMapping(method = RequestMethod.GET, value = "/{threadId}")
    UniThread getOne(@PathVariable Long threadId){
        return uniThreadRepository.findOne(threadId);
    }
}
