package de.uniReddit.uniReddit.GraphQL;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
        UTUser user = authenticateUser();
        if(user==null)
            return new University();
        University university = new University(name, location);
        universityRepository.save(university);
        return university;
    }

    public UniSubject writeUniSubject( String name, UUID universityId){
        University university = universityRepository.findOne(universityId);
        UTUser user = authenticateUser(university);
        if(user==null)return new UniSubject();
        UniSubject uniSubject = new UniSubject(name, university);
        uniSubjectRepository.save(uniSubject);
        return uniSubject;
    }
    public UniThread writeUniThread(String title, UUID uniSubjectId, String content){
        UniSubject uniSubject = uniSubjectRepository.findOne(uniSubjectId);
        University university = uniSubject.getUniversity();
        UTUser user = authenticateUser(university, uniSubject);
        if(user==null)return new UniThread();
        UniThread uniThread = new UniThread(content, user, title, uniSubject);
        threadRepository.save(uniThread);
        return uniThread;
    }

    public Comment writeUniComment(UUID parentId, String content){
        Post post = postRepository.findOne(parentId);
        University university = post.getUniversity();
        UTUser user = authenticateUser(university, post);
        if(user==null)return new Comment();
        Comment comment = new Comment(content, user, post);
        commentRepository.save(comment);
        return comment;
    }

    private UTUser authenticateUser(University university, UniItem uniItem){
        UTUser user = authenticateUser(university);
        if(uniItem==null||user==null
                        && !user.getUniversity().getId().equals(uniItem.getUniversityId())
                        && !user.getRole().equals(Roles.Admin))
            return null;
        return user;
    }

    private UTUser authenticateUser(University university){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser UTUser = userRepository.findByUsername(username);
        if(university==null||UTUser==null
                && !UTUser.getUniversity().getId().equals(university.getId())
                && !UTUser.getRole().equals(Roles.Admin))
            return null;
        return UTUser;
    }
    private UTUser authenticateUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser UTUser = userRepository.findByUsername(username);
        if(UTUser==null||!UTUser.getRole().equals(Roles.Admin))
            return null;
        return UTUser;
    }


}
