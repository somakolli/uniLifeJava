package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Sokol Makolli
 */
@Entity
@DiscriminatorValue("C")
public class Comment extends Post{
    @ManyToOne
    @NotNull
    private Post parent;


    public Comment(String content, UTUser creator, Post parent){
        super(content, creator, parent.getUniversity());
        setParent(parent);
    }

    public Comment() {
        // JPA
    }

    Post getParent() {
        return parent;
    }

    public void setParent(Post parent) {
        this.parent = parent;
        if(!parent.containsChild(this))
            parent.addChild(this);
    }

}
