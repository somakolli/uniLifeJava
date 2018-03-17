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
    //Firebase ID
    @Column(unique = true)
    private String uid;

    @Column
    private String firstName;

    @Column
    private String surName;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String email;

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

    public UTUser(String firstName, String surName, String email, String profilePictureUrl, University university) {
        super(university);
        this.firstName = firstName;
        this.surName = surName;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
    }

    public UTUser(String email, String username, University university) {
        super(university);
        this.email = email;
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

    // @return true if subscribe false if unsubscribe
    public boolean subscribe(UniSubject uniSubject){
        if(subscribedSubjects.contains(uniSubject)){
            uniSubject.setSubscribed(false);
            subscribedSubjects.remove(uniSubject);
            return false;
        }
        else {
            uniSubject.setSubscribed(true);
            subscribedSubjects.add(uniSubject);
            return true;
        }
    }

    public List<UniSubject> getSubscribedSubjects() {
        return new ArrayList<>(subscribedSubjects);
    }

    public long getKarma() {
        return karma;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
