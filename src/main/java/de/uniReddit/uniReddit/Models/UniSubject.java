package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sokol on 06.08.17.
 */
@Entity
@Inheritance
@DiscriminatorValue("U")
public class UniSubject extends UniItem {

    @Column
    private String name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<UTUser> subscribedUTUsers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "uniSubject", fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<UniThread> uniThreads = new ArrayList<>();

    private String description = "";


    public UniSubject() {
    }

    public UniSubject(String name, University university) {
        super(university);
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
