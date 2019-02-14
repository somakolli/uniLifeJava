package de.uniReddit.uniReddit.Models;

public class UtUserBuilder {
    private String firstName;
    private String surName;
    private String email;
    private String username;
    private String password;
    private String profilePictureUrl;
    private University university;

    public UtUserBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UtUserBuilder setSurName(String surName) {
        this.surName = surName;
        return this;
    }

    public UtUserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UtUserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UtUserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UtUserBuilder setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
        return this;
    }

    public UtUserBuilder setUniversity(University university) {
        this.university = university;
        return this;
    }

    public UtUser createUTUser() {
        return new UtUser(firstName, surName, email, profilePictureUrl, university);
    }
}