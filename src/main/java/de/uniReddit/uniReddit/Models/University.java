package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sokol Makolli
 */
@Entity
@DiscriminatorValue("UNI")
public class University extends Node {
    @NotEmpty
    @NotNull
    @Column
    private String name;

    @NotEmpty
    @NotNull
    @Column
    private String location;

    public University() {
        //JPA
    }

    public University(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

}
