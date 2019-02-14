package de.uniReddit.uniReddit.Models;

import javax.persistence.*;

/**
 * @author Sokol Makolli
 */
@Entity
@Inheritance
@DiscriminatorValue("THREAD")
public class UniThread extends Post{
    @Column
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    private UniSubject uniSubject;

    @Transient
    private Long uniSubjectId;

    public String getTitle() {
        return title;
    }

    public UniSubject getUniSubject() {
        return uniSubject;
    }

    public Long getUniSubjectId() {
        return uniSubjectId;
    }

    public void setUniSubjectId(Long uniSubjectId) {
        this.uniSubjectId = uniSubjectId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUniSubject(UniSubject uniSubject) {
        this.uniSubject = uniSubject;
    }

    public UniThread(){
        super();
        //JPA
    }

    public UniThread(String content, UtUser creator, String title, UniSubject uniSubject) {
        super(content, creator, uniSubject.getUniversity());
        this.title = title;
        this.uniSubject = uniSubject;
    }
}
