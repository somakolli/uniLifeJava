package de.uniReddit.uniReddit.Controllers;

import com.sun.xml.internal.bind.v2.model.core.ID;
import de.uniReddit.uniReddit.Models.Post;
import de.uniReddit.uniReddit.Models.UTUser;
import de.uniReddit.uniReddit.Models.UniThread;
import de.uniReddit.uniReddit.Repositories.PostRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by Sokol on 24.11.2017.
 */
@RestController
@RequestMapping("api/upvote")
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    PostController(PostRepository postRepository, UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{postId}")
    ResponseEntity<?> upvote(@PathVariable UUID postId){
        if(!postRepository.exists(postId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Post post = postRepository.findOne(postId);
        post.upvote(userRepository.findByUsername(username));
        postRepository.save(post);
        UTUser creator = post.getCreator();
        creator.setKarma(userRepository.countKarma(creator.getId()));
        userRepository.save(creator);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
