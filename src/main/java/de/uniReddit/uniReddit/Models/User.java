package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    @Column
    private String email;

    @Column
    @NotNull
    @NotEmpty
    private String username;

    @Column
    private AtomicLong karma = new AtomicLong(0);

    @Column
    private Date registeredDate = new Date();

    @OneToMany(mappedBy = "creator")
    private Set<Post> createdPosts = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    private Set<UniSubject> subscribedSubjects = new HashSet<>();

    @ManyToOne
    private University university;

    User(String email, String username)
    {
        this.email = email;
        this.username = username;
    }
    public User() {
        // JPA
    }
    public Long getId() {
        return id;
    }

    public Set<Post> getCreatedPosts(){
        return createdPosts;
    }

    public void setId(Long id) {
        this.id = id;
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

    String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        university.getUsers().add(this);
        this.university = university;
    }

     boolean subscribe(UniSubject uniSubject){
        uniSubject.getSubscribedUsers().add(this);
        return subscribedSubjects.add(uniSubject);
    }

    Set<UniSubject> getSubscribedSubjects() {
        return subscribedSubjects;
    }


    long getKarma() {
        return karma.get();
    }


}
