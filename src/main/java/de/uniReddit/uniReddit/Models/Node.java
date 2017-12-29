package de.uniReddit.uniReddit.Models;


import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "OBJECT_TYPE")
@Table(name = "NODES")
public abstract class Node {
    @Id
    @GeneratedValue
    private UUID id;

    public UUID getId() {
        return id;
    }
}
