package de.uniReddit.uniReddit.Controllers;

import de.uniReddit.uniReddit.Models.PostContent;
import de.uniReddit.uniReddit.Repositories.PostContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/postContent")
public class PostContentController {
    private final PostContentRepository postContentRepository;
    @Autowired
    PostContentController(PostContentRepository postContentRepository){
        this.postContentRepository = postContentRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{postContentId)")
    PostContent get(@PathVariable Long postContentId){
        PostContent postContent = postContentRepository.findOne(postContentId);
        if (postContent.getPost()==null)
            return null;
        return postContentRepository.findOne(postContentId);
    }
}
