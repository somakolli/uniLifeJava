package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sokol on 06.08.17.
 */
@Entity
public class UniSubject {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @ManyToOne
    private University university;

    @JsonIgnore
    @ManyToMany
    private Set<User> subscribedUsers = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "uniSubject")
    private Set<UniThread> uniThreads = new HashSet<>();

    UniSubject() {
    }

    public UniSubject(University university) {
        this.university = university;
    }

    public long getId() {
        return id;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Set<User> getSubscribedUsers() {
        return subscribedUsers;
    }

    public void setSubscribedUsers(Set<User> subscribedUsers) {
        this.subscribedUsers = subscribedUsers;
    }

    public Set<Long> getUniThreadIds(){
        HashSet<Long> ids = new HashSet<>();
        getUniThreads().forEach(thread -> ids.add(thread.getId()));
        return ids;
    }

    public Set<UniThread> getUniThreads() {
        return uniThreads;
    }

    public void setUniThreads(Set<UniThread> uniThreads) {
        this.uniThreads = uniThreads;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
