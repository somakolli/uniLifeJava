package de.uniReddit.uniReddit.GraphQL;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import de.uniReddit.uniReddit.Models.University;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UniversityQuery implements GraphQLQueryResolver{
    private UniversityRepository universityRepository;

    @Autowired
    public UniversityQuery(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public List<University> getUniversities(){
        University university = universityRepository.findAll().get(0);
       return universityRepository.findAll();
    }

}
