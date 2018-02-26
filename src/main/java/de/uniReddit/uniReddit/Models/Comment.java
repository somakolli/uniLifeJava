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


    public Comment(String content, UTUser creator, Post parent){
        super(content, creator, parent.getUniversity());
        setParent(parent);
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
