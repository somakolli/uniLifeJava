package de.uniReddit.uniReddit;

import de.uniReddit.uniReddit.Models.Roles;
import de.uniReddit.uniReddit.Models.University;
import de.uniReddit.uniReddit.Models.User;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


import javax.xml.crypto.Data;

/**
 * Created by Sokol on 26.11.2017.
 */
@Component
public class DataLoader implements ApplicationRunner {
    private UniversityRepository universityRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public DataLoader(UniversityRepository universityRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.universityRepository = universityRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void run(ApplicationArguments args){
        University university = new University.UniversityBuilder()
                .name("Universität Stuttgart").location("Stuttgart").build();
        universityRepository.save(university);
        String password = bCryptPasswordEncoder.encode("password");
        User user = new User.UserBuilder()
                .username("admin").email("info@unitalq.com").password(password).university(university).build();
        user.setRole(Roles.Admin);
        userRepository.save(user);
    }

}
