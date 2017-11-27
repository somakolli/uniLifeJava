package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.boot.autoconfigure.web.ResourceProperties;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private final Date created = new Date();

    @Column
    private Date updated = new Date();

    @ManyToMany
    private Set<User> upvoters = new HashSet<>();

    @Column
    private long upvotes = 0;

    @Column
    private Long contentId;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private Set<Comment> children = new HashSet<>();

    @JsonBackReference
    @ManyToOne
    private User creator;

    Post(Long contentId, User creator)
    {
        this.contentId = contentId;
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

    public Long getContentId(){
        return contentId;
    }

    public long getUpVotes(){
        return this.upvotes;
    }

    public User getCreator(){
        return this.creator;
    }

    public String getCreatorUsername(){
        return this.creator.getUsername();
    }

    public void setContent(Long contentId){
        this.contentId = contentId;
        updated = new Date();
    }

    public void setCreator(User creator) {
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

    public void upvote(User user) {
        if(upvoters.contains(user)){
            upvoters.remove(user);
        }else{
            upvoters.add(user);
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
