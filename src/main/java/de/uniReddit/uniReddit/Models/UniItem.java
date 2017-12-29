package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("I")
@DiscriminatorColumn(name = "UNI_ITEM_TYPE")
@Table(name = "UNI_ITEMS")
public abstract class UniItem extends Node{
    @JsonIgnore
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private University university;

    public UniItem(University university) {
        this.university = university;
    }

    public UniItem() {
    }

    public UUID getUniversityId(){
        return university.getId();
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}
