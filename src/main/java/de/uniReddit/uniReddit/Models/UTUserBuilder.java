package de.uniReddit.uniReddit.Models;

public class UTUserBuilder {
    private String firstName;
    private String surName;
    private String email;
    private String username;
    private String password;
    private String profilePictureUrl;
    private University university;

    public UTUserBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UTUserBuilder setSurName(String surName) {
        this.surName = surName;
        return this;
    }

    public UTUserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UTUserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UTUserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UTUserBuilder setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
        return this;
    }

    public UTUserBuilder setUniversity(University university) {
        this.university = university;
        return this;
    }

    public UTUser createUTUser() {
        return new UTUser(firstName, surName, email, profilePictureUrl, university);
    }
}