package de.uniReddit.uniReddit.Models;

import org.junit.Assert;
import org.junit.Test;

public class CommentTest {
    @Test

    public void parentTest(){
        PostMock post = new PostMock();
        Comment comment = new Comment();
        comment.setParent(post);
        Assert.assertTrue(comment.getParent().equals(post));
        Assert.assertTrue(post.containsChild(comment));
    }
}
