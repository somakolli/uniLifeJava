package de.uniReddit.uniReddit.Models;

import javax.persistence.*;

/**
 * @author Sokol Makolli
 */
@Entity
public class PostContent {
   @Id
   @GeneratedValue
   private long id;

    @Column
    private String content;

    @OneToOne
    private Post post;

    public PostContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    PostContent(){
        // JPA
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString(){
        return content;
    }


    public static final class PostContentBuilder {
        private PostContent postContent;

        public PostContentBuilder() {
            postContent = new PostContent();
        }

        public static PostContentBuilder aPostContent() {
            return new PostContentBuilder();
        }

        public PostContentBuilder content(String content) {
            postContent.setContent(content);
            return this;
        }

        public PostContent build() {
            return postContent;
        }
    }
}
