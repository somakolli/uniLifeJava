package de.uniReddit.uniReddit.Models;

import org.junit.Assert;
import org.junit.Test;

public class PostTest {

    private static final String email = "s.makolli@aol.de";

    private static final String username = "sokol";

    private UTUser UTUser = new UTUser.UserBuilder().username(username).email(email).build();

    private PostMock post = new PostMock("test", UTUser);


    @Test
    public void childrenTest(){
        Comment comment = new Comment();
        comment.setParent(post);
        post.addChild(comment);
        Assert.assertTrue(post.containsChild(comment));
        Assert.assertTrue(comment.getParent().equals(post));
    }
    @Test
    public void createPostTest()
    {
        post.setCreator(UTUser);
        Assert.assertEquals(post.getCreator(), UTUser);
        Assert.assertTrue(UTUser.getCreatedPosts().contains(post));
    }
}
