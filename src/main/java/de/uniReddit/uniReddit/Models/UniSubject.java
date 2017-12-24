package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<UTUser> subscribedUTUsers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "uniSubject", fetch = FetchType.EAGER)
    private List<UniThread> uniThreads = new ArrayList<>();

    @Transient
    private Long universityId;

    public UniSubject() {
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

    public List<UTUser> getSubscribedUTUsers() {
        return subscribedUTUsers;
    }

    public void setSubscribedUTUsers(List<UTUser> subscribedUTUsers) {
        this.subscribedUTUsers = subscribedUTUsers;
    }

    public List<UniThread> getUniThreads() {
        return uniThreads;
    }

    public void setUniThreads(List<UniThread> uniThreads) {
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
