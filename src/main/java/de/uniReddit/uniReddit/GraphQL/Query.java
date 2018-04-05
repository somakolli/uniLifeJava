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

import static de.uniReddit.uniReddit.GraphQL.HelperFunctions.*;

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
        UniThread thread = checkExistance(threadRepository, threadId);
        UTUser user = checkAuthorization(thread.getUniversityId(), userRepository);
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
        UTUser user = checkAuthorization(universityId, userRepository);
        List<UniSubject> uniSubjects = uniSubjectRepository.findAllByUniversity(universityRepository.findOne(universityId),
                new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                        sortProperties));
        for (UniSubject unisubject:
             uniSubjects) {
            if(user.getSubscribedSubjects().contains(unisubject)) unisubject.setSubscribed(true);
        }
        return uniSubjects;
    }
    public List<UniThread> getUniThreads(Long uniSubjectId,
                                         int page, int pageSize,
                                         String sortDirection,
                                         String sortProperties){


        UniSubject uniSubject = checkExistance(uniSubjectRepository, uniSubjectId);;
        UTUser user = checkAuthorization(uniSubject.getUniversityId(), userRepository);
        List<UniThread> threads = threadRepository.findAllByUniSubject(uniSubject, new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                sortProperties));
        checkUpvoted(threads, user, userRepository, postRepository);
        return threads;
    }
    public List<UniThread> getUniThreadsBySubjectName(String uniSubjectName,
                                         Long universityId,
                                         int page, int pageSize,
                                         String sortDirection,
                                         String sortProperties){
        UniSubject uniSubject = checkExistance(uniSubjectRepository, universityRepository, uniSubjectName, universityId);
        UTUser user = checkAuthorization(uniSubject.getUniversityId(), userRepository);
        List<UniThread> threads = threadRepository.findAllByUniSubject(uniSubject, new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                sortProperties));
        checkUpvoted(threads, user, userRepository, postRepository);
        return threads;
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
        checkUpvoted(comments, user, userRepository, postRepository);
        return comments;
    }
    public UniSubject getUniSubject(Long uniSubjectId){
        UniSubject uniSubject = checkExistance(uniSubjectRepository, uniSubjectId);
        UTUser user = checkAuthorization(uniSubject.getUniversityId(), userRepository);
        uniSubject.setSubscribed(user.getSubscribedSubjects().contains(uniSubject));
        return uniSubject;
    }

    public List<UniThread> getMyTimeLine(int page, int pageSize, String sortDirection, String sortProperties){
        UTUser user = getUser(userRepository);
        return threadRepository.findAllByUniSubjectIn(user.getSubscribedSubjects(),
                new PageRequest(page, pageSize, Sort.Direction.fromString(sortDirection),
                        sortProperties));
    }
}
