package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
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

    @JsonIgnore
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private University university;

    @JsonIgnore
    @ManyToMany
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<UTUser> subscribedUTUsers = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "uniSubject")
    private Set<UniThread> uniThreads = new HashSet<>();

    @Transient
    private Long universityId;

    UniSubject() {
    }

    public UniSubject(String name, University university) {
        this.name = name;
        this.university = university;
    }

    public Long getUniversityId() {
        if(university!=null)
            return university.getId();
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
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

    public Set<UTUser> getSubscribedUTUsers() {
        return subscribedUTUsers;
    }

    public void setSubscribedUTUsers(Set<UTUser> subscribedUTUsers) {
        this.subscribedUTUsers = subscribedUTUsers;
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
