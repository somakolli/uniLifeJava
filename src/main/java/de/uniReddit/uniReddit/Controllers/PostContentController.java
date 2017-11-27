package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.Models.PostContent;
import de.uniReddit.uniReddit.Models.Roles;
import de.uniReddit.uniReddit.Models.User;
import de.uniReddit.uniReddit.Repositories.PostContentRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/postContent")
public class PostContentController {
    private final PostContentRepository postContentRepository;
    private final UserRepository userRepository;
    @Autowired
    PostContentController(PostContentRepository postContentRepository, UserRepository userRepository){
        this.postContentRepository = postContentRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{postContentId)")
    ResponseEntity<PostContent> get(@PathVariable Long postContentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        PostContent postContent = postContentRepository.findOne(postContentId);
        User user = userRepository.findByUsername(username);
        if(!user.getRole().equals(Roles.Admin)
                &&!user.getUniversityId().equals(postContent.getUniversityId()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (postContent.getPost()==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(postContentRepository.findOne(postContentId));
    }
}
