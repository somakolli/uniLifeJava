package de.uniReddit.uniReddit.Models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author Sokol Makolli
 */
@Entity
@DiscriminatorValue("C")
public class Comment extends Post{
    @ManyToOne
    @NotEmpty
    @NotNull
    private Post parent;

    public Comment(PostContent content, User creator, Post parent){
        super(content, creator);
        setParent(parent);

    }

    Comment() {
        // JPA
    }

    Post getParent() {
        return parent;
    }

    void setParent(Post parent) {
        this.parent = parent;
        if(!parent.containsChild(this))
            parent.addChild(this);
    }
}
