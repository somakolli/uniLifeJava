package de.uniReddit.uniReddit.Models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

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

    public Comment(String content, User creator, Post parent){
        super(content, creator);
        setParent(parent);

    }

    Comment() {
        // JPA
    }

    public Long getParentId() {
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


    public static final class CommentBuilder {
        private Comment comment;

        private CommentBuilder() {
            comment = new Comment();
        }

        public static CommentBuilder aComment() {
            return new CommentBuilder();
        }

        public CommentBuilder parent(Post parent) {
            comment.setParent(parent);
            return this;
        }

        public CommentBuilder content(String content) {
            comment.setContent(content);
            return this;
        }

        public CommentBuilder creator(User creator) {
            comment.setCreator(creator);
            return this;
        }

        public Comment build() {
            return comment;
        }
    }
}
