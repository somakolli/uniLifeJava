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
            University university = new University("Universität Stuttgart", "Stuttgart");
            universityRepository.save(university);
            String password = bCryptPasswordEncoder.encode("password");
            UTUser user = new UTUser("Sokol",
                    "Makolli",
                    "info@unitalq.com",
                    "admin",
                    password,
                    "",
                    university.getId());
            user.setUniversity(university);
            user.setRole(Roles.Admin);
            user.getCreatedPosts();
            userRepository.save(user);
        }
    }

}
