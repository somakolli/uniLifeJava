package de.uniReddit.uniReddit.Models;


import javax.persistence.*;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "OBJECT_TYPE")
@Table(name = "NODES")
public abstract class Node {
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }
}