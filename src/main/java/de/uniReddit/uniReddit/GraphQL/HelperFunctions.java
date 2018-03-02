package de.uniReddit.uniReddit.GraphQL;

import de.uniReddit.uniReddit.Exceptions.NotAuthenticatedException;
import de.uniReddit.uniReddit.Exceptions.NotAuthorizedException;
import de.uniReddit.uniReddit.Exceptions.ResourceNotFoundException;
import de.uniReddit.uniReddit.Models.Node;
import de.uniReddit.uniReddit.Models.Roles;
import de.uniReddit.uniReddit.Models.UTUser;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;

public class HelperFunctions {
    public static <T extends Node> T checkExistance(JpaRepository<T, Long> repository, Long id){
        Object[] params = {id};
        T t = repository.findOne(id);
        if(t == null) throw new ResourceNotFoundException(params);
        return t;
    }

    public static UTUser getUser(UserRepository userRepository){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UTUser user = userRepository.findByUsername(username);
        if(user == null){
            throw new NotAuthenticatedException();
        }
        return user;
    }

    public static void checkAuthorization(Long universityId, UserRepository userRepository){
        UTUser user = getUser(userRepository);
        if(!user.getUniversityId().equals(universityId)&&!user.getRole().equals(Roles.Admin))
            throw new NotAuthorizedException(universityId);
    }
}
