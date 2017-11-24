package de.uniReddit.uniReddit.Models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
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

    public Comment(Long contentId, User creator, Post parent){
        super(contentId, creator);
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

        public CommentBuilder content(Long contentId) {
            comment.setContent(contentId);
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
