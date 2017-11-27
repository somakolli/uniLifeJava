package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Sokol on 23.11.2017.
 */
@RestController
@RequestMapping("api/comments")
public class CommentController {
    private final UserRepository userRepository;
    private final UniSubjectRepository uniSubjectRepository;
    private final UniversityRepository universityRepository;
    private final ThreadRepository uniThreadRepository;
    private final PostRepository postRepository;
    private final PostContentRepository postContentRepository;
    private final CommentRepository commentRepository;

    @Autowired
    CommentController(UserRepository userRepository,
                        UniSubjectRepository uniSubjectRepository,
                        UniversityRepository universityRepository,
                        ThreadRepository threadRepository,
                        PostContentRepository postContentRepository,
                        CommentRepository commentRepository,
                        PostRepository postRepository
    ){
        this.userRepository = userRepository;
        this.uniSubjectRepository = uniSubjectRepository;
        this.universityRepository = universityRepository;
        this.uniThreadRepository = threadRepository;
        this.postContentRepository = postContentRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestParam String content,@RequestParam Long parentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(!postRepository.exists(parentId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("parent not found");
        PostContent contentO = PostContent.PostContentBuilder.aPostContent().content(content).build();
        postContentRepository.save(contentO);
        Post parentPost = postRepository.findOne(parentId);
        User user = userRepository.findByUsername(username);

        Comment comment = Comment.CommentBuilder.aComment().content(contentO.getId()).parent(parentPost).creator(user).build();
        parentPost.addChild(comment);
        commentRepository.save(comment);
        postRepository.save(parentPost);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @RequestMapping(method = RequestMethod.GET, value = "/{commentId}")
    Comment get(@PathVariable Long commentId){
        return commentRepository.findOne(commentId);
    }


    @RequestMapping(method = RequestMethod.PUT)
    ResponseEntity<?> update(@RequestParam Long commentId,
                             @RequestParam String content){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(!commentRepository.exists(commentId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("comment not found");

        Comment comment = commentRepository.findOne(commentId);
        if(!comment.getCreator().getUsername().equals(username))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Long postContentId = comment.getContentId();
        PostContent postContent = postContentRepository.findOne(postContentId);
        postContent.setContent(content);
        postContentRepository.save(postContent);
        comment.setCreator(userRepository.findByUsername(username));

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/{commentId}")
    ResponseEntity<?> delete(@PathVariable Long commentId){
        if(!commentRepository.exists(commentId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("comment not found");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(!commentRepository.getOne(commentId).getCreator().getUsername().equals(username)
                ||userRepository.findByUsername(username).getRole()!=Roles.Admin)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        commentRepository.delete(commentId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
