package de.uniReddit.uniReddit.GraphQL;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import de.uniReddit.uniReddit.Models.University;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {
    private UniversityRepository universityRepository;

    @Autowired
    public Mutation(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }


    public University writeUniversity(String name, String location){
        University university = new University(name, location);
        universityRepository.save(university);
        return university;
    }
}
