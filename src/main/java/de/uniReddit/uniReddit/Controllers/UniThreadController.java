package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.Models.PostContent;
import de.uniReddit.uniReddit.Models.UniSubject;
import de.uniReddit.uniReddit.Models.UniThread;
import de.uniReddit.uniReddit.Models.User;
import de.uniReddit.uniReddit.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        User author = userRepository.findOne(authorId);
        UniSubject uniSubject = uniSubjectRepository.findOne(subjectId);
        PostContent content = new PostContent.PostContentBuilder().content(contentString).build();
        UniThread thread = new UniThread.UniThreadBuilder().content(content).creator(author).title(title).uniSubject(uniSubject).build();
        postContentRepository.save(content);
        uniThreadRepository.save(thread);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
