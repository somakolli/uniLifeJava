package de.uniReddit.uniReddit.Models;

import org.junit.Assert;
import org.junit.Test;

public class PostTest {

    private static final String email = "s.makolli@aol.de";

    private static final String username = "sokol";

    private User user = new User.UserBuilder().username(username).email(email).build();

    private PostMock post = new PostMock("test",user);


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
        post.setCreator(user);
        Assert.assertEquals(post.getCreator(), user);
        Assert.assertTrue(user.getCreatedPosts().contains(post));
    }

    @Test
    public void upvotePostTest()
    {
        long karma = user.getKarma();
        long upvotes = post.getUpvotes();
        post.upvote(user);
        Assert.assertEquals(upvotes+1, post.getUpvotes());
        Assert.assertEquals(karma + 1, user.getKarma());
        post.upvote(user);
        Assert.assertEquals(upvotes,post.getUpvotes());
        Assert.assertEquals(karma, user.getKarma());

    }
}
