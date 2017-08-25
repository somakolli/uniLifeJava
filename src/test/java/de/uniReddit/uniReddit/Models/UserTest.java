package de.uniReddit.uniReddit.Models;

import org.junit.Assert;
import org.junit.Test;

/**
 *  @author Sokol Makolli
 */
public class UserTest {
    private static final String email = "s.makolli@aol.de";

    private static final String username = "sokol";

    @Test
    public void testMinConstructor(){
        //Before
        User user = new User(email, username);

        Assert.assertEquals(email, user.getEmail());
        Assert.assertEquals(username, user.getUsername());
    }

    @Test
    public void testSubscription(){
        User user = new User(email, username);
        UniSubject uniSubject = new UniSubject(new University("Uni Stuttgart", "Stuttgart"));
        Assert.assertTrue(user.subscribe(uniSubject));
        Assert.assertTrue(user.getSubscribedSubjects().contains(uniSubject));
        Assert.assertTrue(uniSubject.getSubscribedUsers().contains(user));
    }

    @Test
    public void enrollmentTest(){
        User user = new User(email, username);
        University uni = new University("uni stuttgart", "stuttgart");
        user.setUniversity(uni);
        Assert.assertTrue(user.getUniversity().equals(uni));
        Assert.assertTrue(uni.getUsers().contains(user));
    }



}
