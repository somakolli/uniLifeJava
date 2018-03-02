package de.uniReddit.uniReddit.Models;

public class UniSubjectBuilder {
    private String name;
    private University university;

    public UniSubjectBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UniSubjectBuilder setUniversity(University university) {
        this.university = university;
        return this;
    }

    public UniSubject createUniSubject() {
        return new UniSubject(name, university);
    }
}