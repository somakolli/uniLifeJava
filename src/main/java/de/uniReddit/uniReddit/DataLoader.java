package de.uniReddit.uniReddit;

import de.uniReddit.uniReddit.Models.University;
import de.uniReddit.uniReddit.Models.User;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


import javax.xml.crypto.Data;

/**
 * Created by Sokol on 26.11.2017.
 */
@Component
public class DataLoader implements ApplicationRunner {
    private UniversityRepository universityRepository;


    @Autowired
    public DataLoader(UniversityRepository universityRepository){
        this.universityRepository = universityRepository;
    }

    public void run(ApplicationArguments args){
        universityRepository.save(new University.UniversityBuilder()
                .name("Universität Suttgart").location("Stuttgart").build());
    }

}
