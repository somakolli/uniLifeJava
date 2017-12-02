package de.uniReddit.uniReddit;

import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.ThreadRepository;
import de.uniReddit.uniReddit.Repositories.UniSubjectRepository;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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



    @Autowired
    public DataLoader(UniversityRepository universityRepository,
                      UserRepository userRepository,
                      BCryptPasswordEncoder bCryptPasswordEncoder,
                      UniSubjectRepository uniSubjectRepository,
                      ThreadRepository threadRepository){
        this.universityRepository = universityRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.uniSubjectRepository = uniSubjectRepository;
        this.threadRepository = threadRepository;
    }

    public void run(ApplicationArguments args){
        if(!userRepository.existsByUsername("admin")) {
            University university = new University.UniversityBuilder()
                    .name("Universität Stuttgart").location("Stuttgart").build();
            universityRepository.save(university);
            String password = bCryptPasswordEncoder.encode("password");
            UTUser UTUser = new UTUser.UserBuilder()
                    .username("admin").email("info@unitalq.com").password(password).university(university).build();
            UTUser.setRole(Roles.Admin);
            userRepository.save(UTUser);
            UniSubject uniSubject = UniSubjectBuilder.anUniSubject().name("test").university(university).build();
            uniSubjectRepository.save(uniSubject);
            for (long i = 0; i <= 299_999_999; i++) {
                UniThread uniThread = UniThread.UniThreadBuilder.anUniThread().creator(UTUser).title("testThread " + i).uniSubject(uniSubject).build();
                uniThread.upvote(UTUser);
                threadRepository.save(uniThread);
            }
            userRepository.save(UTUser);
        }
    }

}
