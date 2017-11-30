package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.Valid;
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
public class User{

    @Id
    @GeneratedValue
    @JsonView(View.Authorized.class)
    private Long id;


    @NotNull
    @NotEmpty
    @Column(unique = true)
    @JsonView(View.Everyone.class)
    private String email;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    @JsonView(View.Everyone.class)
    private String username;

    @NotNull
    @NotEmpty
    @Column
    private String password;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    @JsonView(View.Authorized.class)
    private Roles role = Roles.User;

    @Column
    @JsonView(View.Everyone.class)
    private AtomicLong karma = new AtomicLong(0);

    @Column
    @JsonView(View.Authorized.class)
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

    @Transient
    private Long universityId;

    public User() {
        // JPA
    }

    public Long getId() {
        return id;
    }

    public Set<Post> getCreatedPosts(){
        return createdPosts;
    }

    public Set<Long> getCreatedPostIds(){
        HashSet<Long> ids = new HashSet<>();
        getCreatedPosts().forEach(user -> ids.add(user.getId()));
        return ids;
    }


    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

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
        if(university!=null)
            return university.getId();
        return universityId;
    }



    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
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
            karma += post.getUpvotes();
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
