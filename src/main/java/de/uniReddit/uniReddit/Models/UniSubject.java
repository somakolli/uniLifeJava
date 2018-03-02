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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
