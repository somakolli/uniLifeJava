package de.uniReddit.uniReddit.Models;

public class CommentBuilder {
    private String content;
    private UtUser creator;
    private Post parent;

    public CommentBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public CommentBuilder setCreator(UtUser creator) {
        this.creator = creator;
        return this;
    }

    public CommentBuilder setParent(Post parent) {
        this.parent = parent;
        return this;
    }

    public Comment createComment() {
        return new Comment(content, creator, parent);
    }
}