package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Sokol Makolli
 */

@Entity
public class User extends UserMin {


    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String email;

    @NotNull
    @NotEmpty
    @Column
    private String password;

    @Column
    private AtomicLong karma = new AtomicLong(0);

    @Column
    private Date registeredDate = new Date();

    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    private Set<Post> createdPosts = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    private Set<UniSubject> subscribedSubjects = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    private University university;

    public User() {
        // JPA
    }

    public Set<Post> getCreatedPosts(){
        return createdPosts;
    }

    public Set<Long> getCreatedPostIds(){
        HashSet<Long> ids = new HashSet<>();
        getCreatedPosts().forEach(user -> ids.add(user.getId()));
        return ids;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegisteredDate()
    {
        return registeredDate;
    }

    public University getUniversity() {
        return university;
    }

    public Long getUniversityId() {
        return getUniversity().getId();
    }

    public void setUniversity(University university) {
        university.getUsers().add(this);
        this.university = university;
    }

    public boolean subscribe(UniSubject uniSubject){
        uniSubject.getSubscribedUsers().add(this);
        return subscribedSubjects.add(uniSubject);
    }

    public Set<UniSubject> getSubscribedSubjects() {
        return subscribedSubjects;
    }

    public Set<Long> getSubscribedSubjectsIds(){
        HashSet<Long> ids = new HashSet<>();
        getSubscribedSubjects().forEach(user -> ids.add(user.getId()));
        return ids;
    }

    public long getKarma() {
        return karma.get();
    }

    public long updateKarma() {
        long karma = 0;
        for (Post post :
                createdPosts) {
            karma += post.getUpVotes();
        }
        this.karma.set(karma);
        return this.karma.get();
    }

    public void unSubscribe(UniSubject uniSubject) {
        uniSubject.getSubscribedUsers().remove(this);
        subscribedSubjects.remove(uniSubject);
    }


    public static final class UserBuilder {
        private User user;

        public UserBuilder() {
            user = new User();
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder email(String email) {
            user.setEmail(email);
            return this;
        }

        public UserBuilder username(String username) {
            user.setUsername(username);
            return this;
        }

        public UserBuilder university(University university) {
            user.setUniversity(university);
            return this;
        }

        public UserBuilder password(String password) {
            user.setPassword(password);
            return this;
        }

        public User build() {
            return user;
        }
    }
}
