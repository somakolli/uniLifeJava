package de.uniReddit.uniReddit.GraphQL;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser UTUser = userRepository.findByUsername(username);
        if(UTUser==null&!UTUser.getRole().equals(Roles.Admin))
            return new University();
        University university = new University(name, location);
        universityRepository.save(university);
        return university;
    }

    public UniSubject writeUniSubject(Long universityId, String name){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser UTUser = userRepository.findByUsername(username);
        if(UTUser==null&&(UTUser.getUniversity().getId().equals(universityId)&&!UTUser.getRole().equals(Roles.Admin)))
            return new UniSubject();
        University university = universityRepository.findOne(universityId);
        if(university==null)
            return new UniSubject();
        UniSubject uniSubject = new UniSubject(name, university);
        uniSubjectRepository.save(uniSubject);
        return uniSubject;
    }
    public UniThread writeUniThread(String title, Long uniSubjectId, String content){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser UTUser = userRepository.findByUsername(username);
        UniSubject uniSubject = uniSubjectRepository.findOne(uniSubjectId);
        if(uniSubject==null&&UTUser==null&&
                (UTUser.getUniversity().getId().equals(uniSubject.getUniversityId())&&!UTUser.getRole().equals(Roles.Admin)))
            return new UniThread();
        UniThread uniThread = new UniThread(content, UTUser, title, uniSubject);
        threadRepository.save(uniThread);
        return uniThread;
    }

    public Comment writeUniComment(Long postId, String content){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser UTUser = userRepository.findByUsername(username);
        Post post = postRepository.findOne(postId);
        if(post==null&&UTUser==null&&
                (UTUser.getUniversity().getId().equals(post.getUniversityId())&&!UTUser.getRole().equals(Roles.Admin)))
            return new Comment();
        Comment comment = new Comment(content, UTUser, post);
        commentRepository.save(comment);
        return comment;
    }

}
