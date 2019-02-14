package de.uniReddit.uniReddit.Models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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

    public Comment(String content, UtUser creator, Post parent){
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
    }

}
