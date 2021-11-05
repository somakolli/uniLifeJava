package de.uniReddit.uniReddit.Models;

public class UniThreadBuilder {
    private String content;
    private UTUser creator;
    private String title;
    private UniSubject uniSubject;

    public UniThreadBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public UniThreadBuilder setCreator(UTUser creator) {
        this.creator = creator;
        return this;
    }

    public UniThreadBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public UniThreadBuilder setUniSubject(UniSubject uniSubject) {
        this.uniSubject = uniSubject;
        return this;
    }

    public UniThread createUniThread() {
        return new UniThread(content, creator, title, uniSubject);
    }
}