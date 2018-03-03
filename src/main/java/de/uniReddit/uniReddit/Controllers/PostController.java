package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.Models.Post;
import de.uniReddit.uniReddit.Models.UTUser;
import de.uniReddit.uniReddit.Repositories.PostRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by Sokol on 24.11.2017.
 */
@RestController
@RequestMapping("api/upvote")
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate template;

    @Autowired
    PostController(PostRepository postRepository, UserRepository userRepository, SimpMessagingTemplate template){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.template = template;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{postId}")
    ResponseEntity<?> upvote(@PathVariable Long postId){
        if(!postRepository.exists(postId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser user = userRepository.findByUsername(username);
        if(user==null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Post post = postRepository.findOne(postId);
        UTUser creator = post.getCreator();
        if (user.getUpvotedPosts().contains(post)) {
            postRepository.decrementVotesById(postId);
            user.getUpvotedPosts().remove(post);
            template.convertAndSend("/topic/upvotes/"+ postId, post.getUpvotes()-1);
            template.convertAndSend("/topic/upvotes/" + creator.getId(), creator.getKarma()-1);
        } else {
            postRepository.incrementVotesById(postId);
            user.getUpvotedPosts().add(post);
            template.convertAndSend("/topic/upvotes/"+ postId, post.getUpvotes()+1);
            template.convertAndSend("/topic/upvotes/" + creator.getId(), creator.getKarma()+1);
        }
        creator.setKarma(userRepository.countKarma(creator.getId()));
        userRepository.save(creator);
        postRepository.flush();
        post = postRepository.findOne(postId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
