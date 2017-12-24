package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @Transient
    private Long parentId;

    public Comment(String content, UTUser creator, Post parent){
        super(content, creator);
        setParent(parent);

    }

    public Comment() {
        // JPA
    }

    public Long getParentId() {
        if(parent != null)
        return parent.getId();
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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
