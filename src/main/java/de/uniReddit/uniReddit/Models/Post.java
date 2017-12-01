package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author Sokol Makolli
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "POST_TYPE")
@Table(name = "POST")
public abstract class Post {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private final Date created = new Date();

    @Column
    private Date updated = new Date();

    @ManyToMany
    private Set<UTUser> upvoters = new HashSet<>();

    @Column
    @NotNull
    @Order
    private long upvotes = 0;

    @Column
    private String content;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private Set<Comment> children = new HashSet<>();

    @JsonBackReference
    @ManyToOne
    private UTUser creator;

    Post(String content, UTUser creator)
    {
        this.content = content;
        setCreator(creator);
    }

    Post(){
        // JPA
    }

    public Long getUniversityId() {
        return creator.getUniversityId();
    }

    public Long getId() {
        return id;
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
        creator.updateKarma();
    }

    public List<Long> getChildrenIds(){
        List<Long> childrenIds = new ArrayList<>();
        for (Comment comment :
                children) {
            childrenIds.add(comment.getId());
        }
        return childrenIds;
    }


}
