package de.uniReddit.uniReddit.GraphQL;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import de.uniReddit.uniReddit.Exceptions.NotAuthorizedException;
import de.uniReddit.uniReddit.Exceptions.ResourceExistsException;
import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static de.uniReddit.uniReddit.GraphQL.HelperFunctions.checkAuthorization;
import static de.uniReddit.uniReddit.GraphQL.HelperFunctions.checkExistance;
import static de.uniReddit.uniReddit.GraphQL.HelperFunctions.getUser;

@Component
public class Mutation implements GraphQLMutationResolver {
    private UniversityRepository universityRepository;
    private UserRepository userRepository;
    private UniSubjectRepository uniSubjectRepository;
    private ThreadRepository threadRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    @Autowired
    public Mutation(UniversityRepository universityRepository,
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
        this.commentRepository  = commentRepository;
    }


    public University writeUniversity(String name, String location){
        UTUser user = getUser(userRepository);
        if(user.getRole()!=Roles.Admin)
            throw new NotAuthorizedException((long) 0);
        University university = new University(name, location);
        universityRepository.save(university);
        return university;
    }

    public UniSubject writeUniSubject( String name, Long universityId, String description){
        UTUser user = getUser(userRepository);
        checkAuthorization(universityId, userRepository);
        University university = universityRepository.findOne(universityId);
        if(uniSubjectRepository.findByUniversityAndName(university, name)!=null){
            Object[] params = {university, name};
           throw new ResourceExistsException(params);
        }
        UniSubject uniSubject = new UniSubject(name, university);
        uniSubject.setDescription(description);
        uniSubjectRepository.save(uniSubject);
        return uniSubject;
    }
    public UniThread writeUniThread(String title, Long uniSubjectId, String content){
        UTUser user = getUser(userRepository);
        UniSubject uniSubject =checkExistance(uniSubjectRepository, uniSubjectId);
        University university = uniSubject.getUniversity();
        checkAuthorization(uniSubject.getUniversityId(), userRepository);
        UniThread uniThread = new UniThread(content, user, title, uniSubject);
        threadRepository.save(uniThread);
        return uniThread;
    }

    public Comment writeUniComment(Long parentId, String content){
        UTUser user = getUser(userRepository);
        checkExistance(postRepository, parentId);
        Post post = postRepository.findOne(parentId);
        checkAuthorization(post.getUniversityId(), userRepository);
        Comment comment = new Comment(content, user, post);
        commentRepository.save(comment);
        return comment;
    }
}
