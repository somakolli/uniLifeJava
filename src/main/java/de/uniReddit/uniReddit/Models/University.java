package de.uniReddit.uniReddit.Models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sokol Makolli
 */
@Entity
public class University {
    @Id
    @GeneratedValue
    private Long id;

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

    @OneToMany(mappedBy = "university")
    private Set<User> users = new HashSet<>();

    University(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
