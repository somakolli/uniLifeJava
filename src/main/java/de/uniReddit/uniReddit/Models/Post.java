package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Sokol Makolli
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("P")
@DiscriminatorColumn(name = "POST_TYPE")
@Table(name = "POST")
public abstract class Post extends UniItem{
    @Column
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Date created = new Date();

    @Column
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updated = new Date();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<UTUser> upvoters = new ArrayList<>();

    @Column
    @NotNull
    @Order
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long upvotes = 0;

    @Column
    private String content;

    @JsonIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Comment> children = new ArrayList<>();

    @JsonBackReference
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UTUser creator;

    Post(String content, UTUser creator, University university)
    {
        super(university);
        this.content = content;
        setCreator(creator);
    }

    Post(){
        // JPA
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public String getContent(){
        return content;
    }

    public long getUpvotes(){
        return this.upvotes;
    }

    public long getChildrenCount(){
        return children.size();
    }

    public UTUser getCreator(){
        return this.creator;
    }

    public String getCreatorUsername(){
        return this.creator.getUsername();
    }

    public void setContent(String content){
        this.content = content;
        updated = new Date();
    }

    public void setCreator(UTUser creator) {
        creator.getCreatedPosts().add(this);
        this.creator = creator;
    }

    public void addChild(Comment comment){
        if(!comment.getParent().equals(this))
            comment.setParent(this);
        if(!children.contains(comment))
            children.add(comment);
    };

    boolean containsChild(Comment comment){
        return children.contains(comment);
    }

    public void upvote(UTUser UTUser) {
        if(upvoters.contains(UTUser)){
            upvoters.remove(UTUser);
        }else{
            upvoters.add(UTUser);
        }
        upvotes = upvoters.size();
    }
}
