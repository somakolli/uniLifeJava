package de.uniReddit.uniReddit.Models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *  @author Sokol Makolli
 */
public class UtUserTest {
    private static final String email = "s.makolli@aol.de";


    private UtUser UTUser;

    private UniSubject uniSubject;

    private University uni;

    private UniThread thread;

    private Comment comment;

    @Before
    public void setup() {
        uni = new University("Uni Stuttgart", "Stuttgart");
        uni.setId((long)1);
        UTUser = new UtUserBuilder().setEmail(email).setPassword("").setUniversity(uni).createUTUser();
        UTUser.setId((long)2);
        UTUser.setUniversity(uni);
        uniSubject = new UniSubjectBuilder().setName("dsa").setUniversity(uni).createUniSubject();
        uniSubject.setId((long)3);
        thread = new UniThreadBuilder().setContent("Content").setCreator(UTUser).setTitle("Title").setUniSubject(uniSubject).createUniThread();
        thread.setId((long)4);
        comment = new CommentBuilder().setContent("Content").setCreator(UTUser).setParent(thread).createComment();
        comment.setId((long)5);
    }

    @Test
    public void testMinConstructor() {
        Assert.assertEquals(email, UTUser.getEmail());
    }

    @Test
    public void enrollmentTest() {
        UTUser.setUniversity(uni);
        Assert.assertTrue(UTUser.getUniversity().equals(uni));
    }
    @Test
    public void subscribeTest() {
        UTUser.subscribe(uniSubject);
        Assert.assertTrue(UTUser.getSubscribedSubjects().contains(uniSubject));
        UTUser.subscribe(uniSubject);
        Assert.assertTrue(!UTUser.getSubscribedSubjects().contains(uniSubject));
    }
}
