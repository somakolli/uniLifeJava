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
        UTUser user = checkAuthorization(universityId, userRepository);
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
    public UniThread writeUniThread(String title, String uniSubjectName, String content, Long universitId){
        UniSubject uniSubject = checkExistance(uniSubjectRepository, universityRepository, uniSubjectName, universitId);
        UTUser user = checkAuthorization(uniSubject.getUniversityId(), userRepository);
        UniThread uniThread = new UniThread(content, user, title, uniSubject);
        threadRepository.save(uniThread);
        return uniThread;
    }

    public Comment writeUniComment(Long parentId, String content){
        Post post = checkExistance(postRepository, parentId);
        UTUser user = checkAuthorization(post.getUniversityId(), userRepository);
        Comment comment = new Comment(content, user, post);
        commentRepository.save(comment);
        return comment;
    }
    public Post upvote(Long postId){
        Post post = checkExistance(postRepository, postId);
        UTUser user = checkAuthorization(post.getUniversityId(), userRepository);
        UTUser creator = post.getCreator();
        if (user.getUpvotedPosts().contains(post)) {
            postRepository.decrementVotesById(postId);
            user.getUpvotedPosts().remove(post);

        } else {
            postRepository.incrementVotesById(postId);
            user.getUpvotedPosts().add(post);
        }
        creator.setKarma(userRepository.countKarma(creator.getId()));
        userRepository.save(creator);
        postRepository.flush();
        return postRepository.findOne(postId);
    }
    public UTUser setUniversity(Long universityId){
        UTUser user = HelperFunctions.getUser(userRepository);
        University university = HelperFunctions.checkExistance(universityRepository, universityId);
        user.setUniversity(university);
        userRepository.save(user);
        return user;
    }

    public UniSubject subscribe(Long unisubjectId) {
        UniSubject uniSubject = checkExistance(uniSubjectRepository, unisubjectId);
        UTUser user = HelperFunctions.checkAuthorization(uniSubject.getUniversityId(), userRepository);
        user.subscribe(uniSubject);
        userRepository.save(user);
        return uniSubject;
    }
}
