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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
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
    @JsonIgnore
    @OneToMany(mappedBy = "university")
    private Set<UTUser> uTUsers = new HashSet<>();

    University(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }
    @JsonIgnore
    public Set<UTUser> getUTUsers() {
        return uTUsers;
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

    @JsonIgnore
    public HashSet<Long> getUserIds(){
        HashSet<Long> ids = new HashSet<>();
        getUTUsers().forEach(user -> ids.add(user.getId()));
        return ids;
    }

    public static final class UniversityBuilder {
        private University university;

        public UniversityBuilder() {
            university = new University();
        }

        public static UniversityBuilder anUniversity() {
            return new UniversityBuilder();
        }

        public UniversityBuilder name(String name) {
            university.setName(name);
            return this;
        }

        public UniversityBuilder location(String location) {
            university.setLocation(location);
            return this;
        }

        public University build() {
            return university;
        }
    }
}
