package de.uniReddit.uniReddit.Models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *  @author Sokol Makolli
 */
public class UserTest {
    private static final String email = "s.makolli@aol.de";

    private static final String username = "sokol";

    private User user;

    private UniSubject uniSubject;

    private University uni;

    @Before
    public void setup(){
        user = new User.UserBuilder().username(username).email(email).build();
        uni = new University("Uni Stuttgart", "Stuttgart");
        uniSubject = new UniSubject(uni);


    }


    @Test
    public void testMinConstructor(){
        Assert.assertEquals(email, user.getEmail());
        Assert.assertEquals(username, user.getUsername());
    }

    @Test
    public void testSubscription(){
        Assert.assertTrue(user.subscribe(uniSubject));
        Assert.assertTrue(user.getSubscribedSubjects().contains(uniSubject));
        Assert.assertTrue(uniSubject.getSubscribedUsers().contains(user));
    }

    @Test
    public void testUnsubscribe(){
        user.unSubscribe(uniSubject);
        Assert.assertFalse(user.getSubscribedSubjects().contains(uniSubject));
        Assert.assertFalse(uniSubject.getSubscribedUsers().contains(user));
    }

    @Test
    public void enrollmentTest(){
        user.setUniversity(uni);
        Assert.assertTrue(user.getUniversity().equals(uni));
        Assert.assertTrue(uni.getUsers().contains(user));
    }



}
