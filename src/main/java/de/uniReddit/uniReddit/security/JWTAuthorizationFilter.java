package de.uniReddit.uniReddit.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import de.uniReddit.uniReddit.Models.Roles;
import de.uniReddit.uniReddit.Models.UtUser;
import de.uniReddit.uniReddit.Repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static de.uniReddit.uniReddit.security.SecurityConstants.*;

/**
 * Created by Sokol on 26.11.2017.
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter{
    private UserRepository userRepository;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository){
        super(authenticationManager);
        this.userRepository = userRepository;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException{
        String header = request.getHeader(HEADER_STRING);

        if(header == null || !header.startsWith(TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            authenticationToken = getAuthentication(request);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws ExecutionException, InterruptedException {
        String token = request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX, "");
        if(token != null){
            FirebaseToken userCreds = FirebaseAuth.getInstance().verifyIdTokenAsync(token).get();
            String uid = userCreds.getUid();
            String email = userCreds.getEmail();
            String name = userCreds.getName();
            String picture = userCreds.getPicture();
            if(uid != null){
                if(!userRepository.existsByEmail(email)){
                    UtUser user = new UtUser();
                    user.setEmail(email);
                    user.setFirstName(name);
                    user.setProfilePictureUrl(picture);
                    user.setUid(uid);
                    if(uid.equals("kQ9WWffpdVZSBWax50zEcd0MDpG2")){
                        user.setRole(Roles.Admin);
                    }
                    userRepository.save(user);
                }
                return new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
            }
            return null;
        }

        return null;
    }


}
