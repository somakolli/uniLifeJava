package de.uniReddit.uniReddit.Models;

import java.util.Set;

/**
 * Created by Sokol on 28.09.2017.
 */
public final class UniSubjectBuilder {
    private UniSubject uniSubject;

    private UniSubjectBuilder() {
        uniSubject = new UniSubject();
    }

    public static UniSubjectBuilder anUniSubject() {
        return new UniSubjectBuilder();
    }

    public UniSubjectBuilder name(String name) {
        uniSubject.setName(name);
        return this;
    }

    public UniSubjectBuilder university(University university) {
        uniSubject.setUniversity(university);
        return this;
    }

    public UniSubjectBuilder subscribedUsers(Set<UTUser> subscribedUTUsers) {
        uniSubject.setSubscribedUTUsers(subscribedUTUsers);
        return this;
    }

    public UniSubjectBuilder uniThreads(Set<UniThread> uniThreads) {
        uniSubject.setUniThreads(uniThreads);
        return this;
    }

    public UniSubject build() {
        return uniSubject;
    }
}
