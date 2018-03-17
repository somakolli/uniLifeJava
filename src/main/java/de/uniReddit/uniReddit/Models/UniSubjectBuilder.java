package de.uniReddit.uniReddit.Models;

public class UniSubjectBuilder {
    private String name;
    private University university;
    private String description;

    public UniSubjectBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UniSubjectBuilder setUniversity(University university) {
        this.university = university;
        return this;
    }

    public UniSubjectBuilder setDescription(String description){
        this.description = description;
        return this;
    }

    public UniSubject createUniSubject() {
        return new UniSubject(university, name, description);
    }
}