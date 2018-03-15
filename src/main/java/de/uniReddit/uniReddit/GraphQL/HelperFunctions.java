package de.uniReddit.uniReddit.GraphQL;

import de.uniReddit.uniReddit.Exceptions.NotAuthenticatedException;
import de.uniReddit.uniReddit.Exceptions.NotAuthorizedException;
import de.uniReddit.uniReddit.Exceptions.ResourceNotFoundException;
import de.uniReddit.uniReddit.Models.*;
import de.uniReddit.uniReddit.Repositories.UniSubjectRepository;
import de.uniReddit.uniReddit.Repositories.UniversityRepository;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;

public class HelperFunctions {
    public static <T extends Node> T checkExistance(JpaRepository<T, Long> repository, Long id){
        Object[] params = {id};
        T t = repository.findOne(id);
        if(t == null) throw new ResourceNotFoundException(params);
        return t;
    }

    public static UniSubject checkExistance(UniSubjectRepository uniSubjectRepository,
                                            UniversityRepository universityRepository,
                                            String uniSubjectName, Long universityId){
        University university = checkExistance(universityRepository, universityId);
        UniSubject uniSubject = uniSubjectRepository.findByUniversityAndName(university, uniSubjectName);
        Object[] params = {university.getId(), uniSubjectName};
        if(uniSubject==null)
            throw new ResourceNotFoundException(params);
        return uniSubject;
    }

    public static UTUser getUser(UserRepository userRepository){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser user = userRepository.findByEmail(email);
        if(user == null){
            throw new NotAuthenticatedException();
        }
        return user;
    }

    public static UTUser checkAuthorization(Long universityId, UserRepository userRepository){
        UTUser user = getUser(userRepository);
        if(!user.getUniversityId().equals(universityId)&&!user.getRole().equals(Roles.Admin))
            throw new NotAuthorizedException(universityId);
        return user;
    }
}
