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
    @NotEmpty
    @NotNull
    private Post parent;

    public Comment(PostContent content, User creator, Post parent){
        super(content, creator);
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

        public CommentBuilder content(PostContent content) {
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
