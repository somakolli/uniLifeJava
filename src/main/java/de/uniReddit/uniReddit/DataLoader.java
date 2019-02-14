package de.uniReddit.uniReddit;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * Created by Sokol on 26.11.2017.
 */
@Component
public class DataLoader implements ApplicationRunner {
    private UniversityRepository universityRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UniSubjectRepository uniSubjectRepository;
    private ThreadRepository threadRepository;
    private CommentRepository commentRepository;



    @Autowired
    public DataLoader(UniversityRepository universityRepository,
                      UserRepository userRepository,
                      BCryptPasswordEncoder bCryptPasswordEncoder,
                      UniSubjectRepository uniSubjectRepository,
                      ThreadRepository threadRepository,
                      CommentRepository commentRepository){
        this.universityRepository = universityRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.uniSubjectRepository = uniSubjectRepository;
        this.threadRepository = threadRepository;
        this.commentRepository = commentRepository;
    }

    public void run(ApplicationArguments args){
        if(!userRepository.existsByEmail("admin")) {
            //create dummy universities
            University university = new University("Universität Stuttgart", "Stuttgart");
            universityRepository.save(university);

            //create dummy users
            UtUser sokol = new UtUserBuilder().setEmail("info@unitalq.com").setFirstName("Sokol").setUsername("admin")
                    .setSurName("Makolli").setProfilePictureUrl("https://assets.vg247.com/current//2016/07/metal_gear_solid_3.jpg")
                    .setPassword(bCryptPasswordEncoder.encode("password")).createUTUser();
            sokol.setUniversity(university);
            sokol.setRole(Roles.Admin);
            userRepository.save(sokol);

            List<UtUser> users =  createDummyUsers(5, university);
            List<UniSubject> subjects = createDummySubjects(10, university);
            for (UniSubject subject: subjects) {
                sokol.subscribe(subject);
                uniSubjectRepository.save(subject);
            }
            userRepository.save(sokol);
            List<UniThread> threads = createDummyThreads(100, users, subjects);
            List<Comment> comments = createDummyComments(100, users, threads);
            System.out.println("Dummy data created.");
        }
    }

    public List<UtUser> createDummyUsers(int number, University university){
        Lorem lorem = LoremIpsum.getInstance();
        for(int i= 0; i<number; i++){
            UtUser user = new UtUserBuilder()
                    .setUsername(lorem.getName())
                    .setFirstName(lorem.getFirstName())
                    .setSurName(lorem.getLastName())
                    .setPassword(bCryptPasswordEncoder.encode(lorem.getWords(1)))
                    .setEmail(lorem.getEmail())
                    .setUniversity(university)
                    .createUTUser();
            userRepository.save(user);
        }
        return userRepository.findAll();
    }

    public List<UniSubject> createDummySubjects(int number, University university){
        Lorem lorem = LoremIpsum.getInstance();
        for (int i = 0; i < number; i++){
            UniSubject uniSubject = new UniSubjectBuilder()
                    .setUniversity(university)
                    .setName(lorem.getWords(1, 3))
                    .setDescription(lorem.getWords(5,20))
                    .createUniSubject();
            uniSubjectRepository.save(uniSubject);
        }
        return uniSubjectRepository.findAll();
    }

    public List<UniThread> createDummyThreads(int number, List<UtUser> users, List<UniSubject> uniSubjects){
        Lorem lorem = LoremIpsum.getInstance();
        Random random = new Random();
        for (int i = 0; i < number; i++){
            String content = lorem.getWords(10, 100);
            String title = lorem.getWords(4, 10);
            UniThread thread = new UniThreadBuilder()
                    .setContent(content)
                    .setTitle(title)
                    .setCreator(users.get(random.nextInt(users.size()-1)))
                    .setUniSubject(uniSubjects.get(random.nextInt(uniSubjects.size()-1)))
                    .createUniThread();
            threadRepository.save(thread);
        }
        return threadRepository.findAll();
    }

    public List<Comment> createDummyComments(int number, List<UtUser> users, List<UniThread> uniThreads){
        Lorem lorem = LoremIpsum.getInstance();
        Random random = new Random();
        for (int i = 0; i < number; i++){
            Comment comment = new CommentBuilder()
                    .setContent(lorem.getWords(10, 100))
                    .setCreator(users.get(random.nextInt(users.size()-1)))
                    .setParent(uniThreads.get(random.nextInt(uniThreads.size()-1)))
                    .createComment();
            commentRepository.save(comment);
        }
        return commentRepository.findAll();
    }

}
