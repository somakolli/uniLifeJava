package de.uniReddit.uniReddit.security;

import de.uniReddit.uniReddit.Models.UTUser;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Created by Sokol on 26.11.2017.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UTUser UTUser = userRepository.findByUsername(s);

        if(UTUser == null){
            throw new UsernameNotFoundException(s);
        }
        return new org.springframework.security.core.userdetails.User(UTUser.getUsername(),
                UTUser.getPassword(),
                Collections.emptyList());
    }
}
