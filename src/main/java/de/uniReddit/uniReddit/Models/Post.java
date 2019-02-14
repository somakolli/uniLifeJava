package de.uniReddit.uniReddit.Models;

import org.springframework.core.annotation.Order;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Sokol Makolli
 */
@Entity
@DiscriminatorValue("POST")
public abstract class Post extends UniItem{
    @Column
    private int created = (int) (System.currentTimeMillis() / 1000L);;

    @Column
    private int updated = (int) (System.currentTimeMillis() / 1000L);;

    @Column
    @NotNull
    @Order
    private long upvotes = 0;

    @Column(columnDefinition="TEXT")
    private String content;

    @ManyToOne
    private UtUser creator;

    @Column
    private boolean hasComments;

    @Transient
    private boolean upvoted = false;

    Post(String content, UtUser creator, University university) {
        super(university);
        this.content = content;
        setCreator(creator);
    }

    Post(){
        // JPA
    }

    public int getCreated() {
        return created;
    }

    public int getUpdated() {
        return updated;
    }

    public String getContent(){
        return content;
    }

    public long getUpvotes(){
        return this.upvotes;
    }

    public UtUser getCreator(){
        return this.creator;
    }

    public String getShortContent(){
        return content.length() > 100 ? content.substring(0, 100) : content;
    }

    public void setContent(String content){
        this.content = content;
        updated = (int) (System.currentTimeMillis() / 1000L);
    }

    public void setCreator(UtUser creator) {
        this.creator = creator;
    }

    public void addChild(Comment comment){
        if(!comment.getParent().equals(this))
            comment.setParent(this);
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public void setUpvotes(long upvotes) {
        this.upvotes = upvotes;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public boolean isUpvoted() {
        return upvoted;
    }

    public void setUpvoted(boolean upvoted) {
        this.upvoted = upvoted;
    }

    public boolean hasComments() {
        return hasComments;
    }

    public void setHasComments(boolean hasComments) {
        this.hasComments = hasComments;
    }

    @Override
    public boolean equals(Object obj) {
        if(getClass()!=obj.getClass()) return false;
        return this.getId().equals(((Node)obj).getId());
    }
}
