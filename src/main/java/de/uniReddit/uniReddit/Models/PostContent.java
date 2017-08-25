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
}
