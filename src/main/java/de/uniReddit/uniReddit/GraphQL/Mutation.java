package de.uniReddit.uniReddit.GraphQL;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import de.uniReddit.uniReddit.Exceptions.NotAuthorizedException;
import de.uniReddit.uniReddit.Exceptions.ResourceExistsException;
import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
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
        UtUser user = getUser(userRepository);
        if(user.getRole()!=Roles.Admin)
            throw new NotAuthorizedException((long) 0);
        University university = new University(name, location);
        universityRepository.save(university);
        return university;
    }

    public UniSubject writeUniSubject( String name, Long universityId, String description){
        UtUser user = checkAuthorization(universityId, userRepository);
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
        UtUser user = checkAuthorization(uniSubject.getUniversityId(), userRepository);
        UniThread uniThread = new UniThread(content, user, title, uniSubject);
        threadRepository.save(uniThread);
        return uniThread;
    }

    public Comment writeUniComment(Long parentId, String content){
        Post post = checkExistance(postRepository, parentId);
        UtUser user = checkAuthorization(post.getUniversityId(), userRepository);
        Comment comment = new Comment(content, user, post);
        if(!post.hasComments()){
            post.setHasComments(true);
            postRepository.save(post);
        }
        commentRepository.save(comment);
        return comment;
    }
    public Post upvote(Long postId){
        Post post = checkExistance(postRepository, postId);
        UtUser user = checkAuthorization(post.getUniversityId(), userRepository);
        UtUser creator = post.getCreator();
        System.out.println(user.getUpvotedPosts().contains(post));
        if (user.getUpvotedPosts().contains(post)) {
            postRepository.decrementVotesById(postId);
            user.getUpvotedPosts().remove(post);
            post.setUpvotes(post.getUpvotes()-1);
        } else {
            postRepository.incrementVotesById(postId);
            user.getUpvotedPosts().add(post);
            post.setUpvotes(post.getUpvotes()+1);
        }
        creator.setKarma(userRepository.countKarma(creator.getId()));
        userRepository.save(user);
        userRepository.save(creator);
        return post;
    }
    public UtUser setUniversity(Long universityId){
        UtUser user = HelperFunctions.getUser(userRepository);
        University university = HelperFunctions.checkExistance(universityRepository, universityId);
        user.setUniversity(university);
        userRepository.save(user);
        return user;
    }

    public UniSubject subscribe(Long unisubjectId) {
        UniSubject uniSubject = checkExistance(uniSubjectRepository, unisubjectId);
        UtUser user = HelperFunctions.checkAuthorization(uniSubject.getUniversityId(), userRepository);
        if(user.subscribe(uniSubject)){
            uniSubjectRepository.incrementVotesById(unisubjectId);
            uniSubject.setNumberOfSubscribers(uniSubject.getNumberOfSubscribers()+1);
        } else {
            uniSubjectRepository.decrementVotesById(unisubjectId);
            uniSubject.setNumberOfSubscribers(uniSubject.getNumberOfSubscribers()-1);
        }
        userRepository.save(user);
        return uniSubject;
    }
}
