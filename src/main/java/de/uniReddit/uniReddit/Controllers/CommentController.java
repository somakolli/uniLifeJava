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

import javax.ws.rs.Path;
import java.util.Collection;

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
        User user = userRepository.findByUsername(username);
        Post parentPost = postRepository.findOne(parentId);
        if(!postRepository.exists(parentId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("parent not found");
        if(!user.getRole().equals(Roles.Admin)
                &&!user.getUniversityId().equals(parentPost.getUniversityId()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        PostContent contentO = PostContent.PostContentBuilder.aPostContent().content(content).build();
        postContentRepository.save(contentO);
        Comment comment = Comment.CommentBuilder.aComment().content(contentO.getId()).parent(parentPost).creator(user).build();
        parentPost.addChild(comment);
        commentRepository.save(comment);
        postRepository.save(parentPost);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @RequestMapping(method = RequestMethod.GET, value = "/{commentId}")
    ResponseEntity<Comment> get(@PathVariable Long commentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if(!commentRepository.exists(commentId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if(!user.getRole().equals(Roles.Admin)
                &&!user.getUniversityId().equals(commentRepository.getOne(commentId).getUniversityId()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(commentRepository.findOne(commentId));
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<Page<Comment>> getByParentId(@RequestParam Long parentId,
                                                @RequestParam int page,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortDirection,
                                                @RequestParam String sortProperties){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if(!user.getRole().equals(Roles.Admin)
                &&!user.getUniversityId().equals(postRepository.getOne(parentId).getUniversityId()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(commentRepository.findAllByParentId(parentId,
                new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),sortProperties)));
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
                &&userRepository.findByUsername(username).getRole()!=Roles.Admin)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        commentRepository.delete(commentId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
