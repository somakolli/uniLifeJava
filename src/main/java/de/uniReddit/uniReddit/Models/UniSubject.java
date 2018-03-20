package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sokol on 06.08.17.
 */
@Entity
@Inheritance
@DiscriminatorValue("SUBJECT")
public class UniSubject extends UniItem {

    @Column
    private String name;

    @Column
    private String description = "";

    @Column
    private Long numberOfSubscribers = (long)0;

    @OneToMany
    private List<String> tags = new ArrayList<>();

    @Transient
    private boolean subscribed = false;

    public UniSubject() {
    }

    public UniSubject(String name, University university) {
        super(university);
        this.name = name;
    }

    public UniSubject(University university, String name, String description) {
        super(university);
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getSubscribed(){
        return subscribed;
    }

    public void setSubscribed(boolean subscribed){
        this.subscribed = subscribed;
    }
    @Override
    public boolean equals(Object obj) {
        return ((UniSubject) obj).getId().equals(getId());
    }

    public Long getNumberOfSubscribers() {
        return numberOfSubscribers;
    }

    public void setNumberOfSubscribers(Long numberOfSubscribers) {
        this.numberOfSubscribers = numberOfSubscribers;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public List<String> getTags() {
        return tags;
    }

}
