package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("I")
@DiscriminatorColumn(name = "UNI_ITEM_TYPE")
@Table(name = "UNI_ITEMS")
public abstract class UniItem extends Node{
    @ManyToOne
    private University university;

    public UniItem(University university) {
        this.university = university;
    }

    public UniItem() {
    }

    public Long getUniversityId(){
        return university.getId();
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}
