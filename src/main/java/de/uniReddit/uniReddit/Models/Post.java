package de.uniReddit.uniReddit.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.boot.autoconfigure.web.ResourceProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @OneToOne(mappedBy = "post")
    private PostContent content;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private Set<Comment> children = new HashSet<>();

    @JsonBackReference
    @ManyToOne
    private User creator;

    Post(PostContent content, User creator)
    {
        this.content = content;
        setCreator(creator);
    }

    Post(){
        // JPA
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

    public PostContent getContent(){
        return content;
    }

    public long getUpVotes(){
        return this.upvotes;
    }

    public User getCreator(){
        return this.creator;
    }

    public void setContent(PostContent content){
        this.content = content;
        updated = new Date();
    }

    void setCreator(User creator) {
        creator.getCreatedPosts().add(this);
        this.creator = creator;
    }


    void addChild(Comment comment){
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



}
