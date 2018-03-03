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

    private UniThread thread;

    private Comment comment;

    @Before
    public void setup() {
        uni = new University("Uni Stuttgart", "Stuttgart");
        UTUser = new UTUserBuilder().setEmail(email).setUsername(username).setPassword("").setUniversity(uni).createUTUser();
        UTUser.setUniversity(uni);
        uniSubject = new UniSubjectBuilder().setName("dsa").setUniversity(uni).createUniSubject();
        thread = new UniThreadBuilder().setContent("Content").setCreator(UTUser).setTitle("Title").setUniSubject(uniSubject).createUniThread();
        comment = new CommentBuilder().setContent("Content").setCreator(UTUser).setParent(thread).createComment();
    }

    @Test
    public void testMinConstructor() {
        Assert.assertEquals(email, UTUser.getEmail());
        Assert.assertEquals(username, UTUser.getUsername());
    }

    @Test
    public void testSubscription() {
        Assert.assertTrue(UTUser.subscribe(uniSubject));
        Assert.assertTrue(UTUser.getSubscribedSubjects().contains(uniSubject));
    }

    @Test
    public void testUnsubscribe() {
        UTUser.unSubscribe(uniSubject);
        Assert.assertFalse(UTUser.getSubscribedSubjects().contains(uniSubject));
    }

    @Test
    public void enrollmentTest() {
        UTUser.setUniversity(uni);
        Assert.assertTrue(UTUser.getUniversity().equals(uni));
    }

}
