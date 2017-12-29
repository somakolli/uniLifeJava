package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Sokol Makolli
 */

@Entity
@DiscriminatorValue("USER")
public class UTUser extends UniItem{
    @Column
    @JsonView(View.Everyone.class)
    private String firstName;

    @Column
    @JsonView(View.Everyone.class)
    private String surName;


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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column
    @JsonView(View.Everyone.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String profilePictureUrl;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    @JsonView(View.Authorized.class)
    private Roles role = Roles.User;

    @Column
    @JsonView(View.Everyone.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private AtomicLong karma = new AtomicLong(0);

    @Column
    @JsonView(View.Authorized.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date registeredDate = new Date();

    @OneToMany(mappedBy = "creator", fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Post> createdPosts = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<UniSubject> subscribedSubjects = new ArrayList<>();

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

    public List<Post> getCreatedPosts(){
        return createdPosts;
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
        uniSubject.getSubscribedUTUsers().add(this);
        return subscribedSubjects.add(uniSubject);
    }

    public List<UniSubject> getSubscribedSubjects() {
        return subscribedSubjects;
    }

    public long getKarma() {
        return karma.get();
    }

    public void unSubscribe(UniSubject uniSubject) {
        uniSubject.getSubscribedUTUsers().remove(this);
        subscribedSubjects.remove(uniSubject);
    }

    @JsonIgnore
    public void setKarma(long karma) {
        this.karma.set(karma);
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




}
