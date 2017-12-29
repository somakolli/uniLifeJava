package de.uniReddit.uniReddit.GraphQL;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.sun.xml.internal.bind.v2.model.core.ID;
import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.*;
import graphql.GraphQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class Query implements GraphQLQueryResolver{
    private UniversityRepository universityRepository;
    private UserRepository userRepository;
    private UniSubjectRepository uniSubjectRepository;
    private ThreadRepository threadRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private UTUser user;

    @Autowired
    public Query(UniversityRepository universityRepository,
                 UserRepository userRepository,
                 UniSubjectRepository uniSubjectRepository,
                 ThreadRepository threadRepository,
                 PostRepository postRepository,
                 CommentRepository commentRepository) {
        this.universityRepository = universityRepository;
        this.userRepository = userRepository;
        this.uniSubjectRepository = uniSubjectRepository;
        this.threadRepository = threadRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public List<University> getUniversities(){
        return universityRepository.findAll();
    }

    public UTUser getMe() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        user = userRepository.findByUsername(username);
        return user;
    }

    public List<UniSubject> getUniSubjects(UUID universityId,
                                           int page, int pageSize,
                                           String sortDirection,
                                           String sortProperties) throws GraphQLException{
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser UTUser = userRepository.findByUsername(username);
        if(UTUser == null)
            return new ArrayList<UniSubject>();

        if(!UTUser.getUniversityId().equals(universityId)&&!UTUser.getRole().equals(Roles.Admin))
            return new ArrayList<UniSubject>();

        return uniSubjectRepository.findAllByUniversity(universityRepository.findOne(universityId), new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                sortProperties));
    }
    public List<UniThread> getUniThreads(UUID uniSubjectId,
                                         int page, int pageSize,
                                         String sortDirection,
                                         String sortProperties){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser UTUser = userRepository.findByUsername(username);
        if(UTUser == null)
            return new ArrayList<UniThread>();

        UniSubject uniSubject = uniSubjectRepository.findOne(uniSubjectId);
        if(uniSubject==null)
            return new ArrayList<>();
        if(!UTUser.getUniversityId().equals(uniSubject.getUniversityId())&&!UTUser.getRole().equals(Roles.Admin))
            return new ArrayList<>();

        return threadRepository.findAllByUniSubject(uniSubject, new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                sortProperties));
    }

    public List<Comment> getUniComments(UUID postId,
                                        int page, int pageSize,
                                        String sortDirection,
                                        String sortProperties){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser UTUser = userRepository.findByUsername(username);
        if(UTUser == null)
            return new ArrayList<>();
        Post post = postRepository.findOne(postId);
        if(post==null)
            return new ArrayList<>();
        if(!UTUser.getUniversityId().equals(post.getUniversityId())&&!UTUser.getRole().equals(Roles.Admin))
            return new ArrayList<>();

        return commentRepository.findAllByParent(post, new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                sortProperties));
    }
}
