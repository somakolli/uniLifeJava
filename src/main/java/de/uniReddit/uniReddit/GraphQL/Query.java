package de.uniReddit.uniReddit.GraphQL;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import de.uniReddit.uniReddit.Exceptions.NotAuthenticatedException;
import de.uniReddit.uniReddit.Exceptions.NotAuthorizedException;
import de.uniReddit.uniReddit.Exceptions.ResourceNotFoundException;
import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.*;
import graphql.GraphQLException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static de.uniReddit.uniReddit.GraphQL.HelperFunctions.checkAuthorization;
import static de.uniReddit.uniReddit.GraphQL.HelperFunctions.checkExistance;
import static de.uniReddit.uniReddit.GraphQL.HelperFunctions.getUser;

@Component
public class Query implements GraphQLQueryResolver{
    private UniversityRepository universityRepository;
    private UserRepository userRepository;
    private UniSubjectRepository uniSubjectRepository;
    private ThreadRepository threadRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

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

    public UniThread getUniThread(Long threadId){
        UTUser user = checkAuthorization(threadId, userRepository);
        UniThread thread = checkExistance(threadRepository, threadId);
        if(user.getUpvotedPosts().contains(thread)) thread.setUpvoted(true);
        return thread;
    }

    public UTUser getMe() {
        return getUser(userRepository);
    }

    public List<UniSubject> getUniSubjects(Long universityId,
                                          int page, int pageSize,
                                          String sortDirection,
                                          String sortProperties) throws GraphQLException{

        checkExistance(universityRepository, universityId);
        checkAuthorization(universityId, userRepository);
        return uniSubjectRepository.findAllByUniversity(universityRepository.findOne(universityId),
                new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                sortProperties));
    }
    public List<UniThread> getUniThreads(Long uniSubjectId,
                                         int page, int pageSize,
                                         String sortDirection,
                                         String sortProperties){


        UniSubject uniSubject = checkExistance(uniSubjectRepository, uniSubjectId);;
        UTUser user = checkAuthorization(uniSubject.getUniversityId(), userRepository);
        List<UniThread> threads = threadRepository.findAllByUniSubject(uniSubject, new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                sortProperties));
        for (UniThread thread:
             threads) {
            if(user.getUpvotedPosts().contains(thread)) thread.setUpvoted(true);
        }
        return threads;
    }
    public List<UniThread> getUniThreadsBySubjectName(String uniSubjectName,
                                         Long universityId,
                                         int page, int pageSize,
                                         String sortDirection,
                                         String sortProperties){

        University university = checkExistance(universityRepository, universityId);
        UniSubject uniSubject = uniSubjectRepository.findByUniversityAndName(university, uniSubjectName);
        Object[] params = {university.getId(), uniSubjectName};
        if(uniSubject==null)
            throw new ResourceNotFoundException(params);
        checkAuthorization(uniSubject.getUniversityId(), userRepository);

        return threadRepository.findAllByUniSubject(uniSubject, new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                sortProperties));
    }

    public List<Comment> getUniComments(Long postId,
                                        int page, int pageSize,
                                        String sortDirection,
                                        String sortProperties){
        Post post = checkExistance(postRepository, postId);
        UTUser user = checkAuthorization(post.getUniversityId(), userRepository);
        List<Comment> comments = commentRepository.findAllByParent(postRepository.findOne(postId),
                new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                        sortProperties));
        for (Comment comment:
                comments) {
            if(user.getUpvotedPosts().contains(comment)) comment.setUpvoted(true);
        }
        return comments;
    }
}
