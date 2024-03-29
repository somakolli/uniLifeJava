package de.uniReddit.uniReddit.Models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * @author Sokol Makolli
 */
@Entity
@DiscriminatorValue("UNIVERSITY")
public class University extends Node {
    @NotEmpty
    @NotNull
    @Column
    private String name;

    @NotEmpty
    @NotNull
    @Column
    private String location;

    private University() {
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
