package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Sokol Makolli
 */

@Entity
@DiscriminatorValue("USER")
public class UTUser extends UniItem{
    @Column
    private String firstName;

    @Column
    private String surName;


    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String email;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String username;

    @NotNull
    @NotEmpty
    @Column
    private String password;

    @Column
    private String profilePictureUrl;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private Roles role = Roles.User;

    @Column
    private long karma = 0;

    @Column
    private Date registeredDate = new Date();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UniSubject> subscribedSubjects = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Post> upvotedPosts = new HashSet<>();

    public UTUser() {
        // JPA
    }

    public UTUser(String firstName, String surName, String email, String username, String password, String profilePictureUrl, University university) {
        super(university);
        this.firstName = firstName;
        this.surName = surName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.profilePictureUrl = profilePictureUrl;
    }

    public UTUser(String email, String username, String password, University university) {
        super(university);
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
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

    public boolean subscribe(UniSubject uniSubject){
        return subscribedSubjects.add(uniSubject);
    }

    public List<UniSubject> getSubscribedSubjects() {
        return new ArrayList<>(subscribedSubjects);
    }

    public long getKarma() {
        return karma;
    }

    public void unSubscribe(UniSubject uniSubject) {
        subscribedSubjects.remove(uniSubject);
    }

    public void setKarma(long karma) {
        this.karma=karma;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Set<Post> getUpvotedPosts() {
        return upvotedPosts;
    }
}
