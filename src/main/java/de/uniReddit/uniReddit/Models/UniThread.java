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

    UniThread(){
        super();
        //JPA
    }

    public UniThread(String content, UTUser creator, String title, UniSubject uniSubject) {
        super(content, creator);
        this.title = title;
        this.uniSubject = uniSubject;
    }

}
