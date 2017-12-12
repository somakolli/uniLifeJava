package de.uniReddit.uniReddit.Models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *  @author Sokol Makolli
 */
public class UTUserTest {
    private static final String email = "s.makolli@aol.de";

    private static final String username = "sokol";

    private UTUser UTUser;

    private UniSubject uniSubject;

    private University uni;

    @Before
    public void setup(){
        uni = new University("Uni Stuttgart", "Stuttgart");
        UTUser = new UTUser(email, username, "", uni);
        uniSubject = new UniSubject("dsa", uni);

    }


    @Test
    public void testMinConstructor(){
        Assert.assertEquals(email, UTUser.getEmail());
        Assert.assertEquals(username, UTUser.getUsername());
    }

    @Test
    public void testSubscription(){
        Assert.assertTrue(UTUser.subscribe(uniSubject));
        Assert.assertTrue(UTUser.getSubscribedSubjects().contains(uniSubject));
        Assert.assertTrue(uniSubject.getSubscribedUTUsers().contains(UTUser));
    }

    @Test
    public void testUnsubscribe(){
        UTUser.unSubscribe(uniSubject);
        Assert.assertFalse(UTUser.getSubscribedSubjects().contains(uniSubject));
        Assert.assertFalse(uniSubject.getSubscribedUTUsers().contains(UTUser));
    }

    @Test
    public void enrollmentTest(){
        UTUser.setUniversity(uni);
        Assert.assertTrue(UTUser.getUniversity().equals(uni));
        Assert.assertTrue(uni.getUTUsers().contains(UTUser));
    }



}
