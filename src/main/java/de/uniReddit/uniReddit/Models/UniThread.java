package de.uniReddit.uniReddit.Models;

import javax.persistence.*;

/**
 * @author Sokol Makolli
 */
@Entity
@Inheritance
@DiscriminatorValue("T")
public class UniThread extends Post{
    @Column
    private String title;

    @ManyToOne
    private UniSubject uniSubject;

    public UniThread(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUniSubject(UniSubject uniSubject) {
        this.uniSubject = uniSubject;
    }

    UniThread(){
        super();
        //JPA
    }

    public UniThread(PostContent content, User creator, String title, UniSubject uniSubject) {
        super(content, creator);
        this.title = title;
        this.uniSubject = uniSubject;
    }


    public static final class UniThreadBuilder {
        private UniThread uniThread;

        private UniThreadBuilder() {
            uniThread = new UniThread();
        }

        public static UniThreadBuilder anUniThread() {
            return new UniThreadBuilder();
        }

        public UniThreadBuilder title(String title) {
            uniThread.setTitle(title);
            return this;
        }

        public UniThreadBuilder uniSubject(UniSubject uniSubject) {
            uniThread.setUniSubject(uniSubject);
            return this;
        }

        public UniThreadBuilder content(PostContent content) {
            uniThread.setContent(content);
            return this;
        }

        public UniThreadBuilder creator(User creator) {
            uniThread.setCreator(creator);
            return this;
        }

        public UniThread build() {
            return uniThread;
        }
    }
}
