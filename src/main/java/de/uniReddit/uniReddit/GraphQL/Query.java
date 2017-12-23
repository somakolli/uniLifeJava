package de.uniReddit.uniReddit.GraphQL;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import de.uniReddit.uniReddit.Models.UTUser;
import de.uniReddit.uniReddit.Models.University;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Query implements GraphQLQueryResolver{
    private UniversityRepository universityRepository;
    private UserRepository userRepository;
    private UTUser user;

    @Autowired
    public Query(UniversityRepository universityRepository, UserRepository userRepository) {
        this.universityRepository = universityRepository;
        this.userRepository = userRepository;
    }

    public List<University> getUniversities(){

        University university = universityRepository.findAll().get(0);
        return universityRepository.findAll();
    }

    public UTUser getMe() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        user = userRepository.findByUsername(username);
        user.getCreatedPosts();
        user.getSubscribedSubjects();
        return user;
    }
}
