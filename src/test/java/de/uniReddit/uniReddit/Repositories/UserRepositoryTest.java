package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE )
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private ThreadRepository threadRepository;

    static private User user;
    static private University uni;
    static private PostContent content;
    static private UniSubject uniSubject;
    static private UniThread thread;
    String password = "password";
    String email = "s.makolli@aol.de";
    String username = "sokol";

    @Before
    public void setup(){


        uni = new University();
        uni.setLocation("Stuttgart");
        uni.setName("Uni Stuttgart");
        content = new PostContent("Hallo");
        uniSubject = new UniSubject(uni);
        user = new User.UserBuilder().email(email).username(username).university(uni).password(password).build();
        thread = new UniThread(content.getId(),user, "Test",  uniSubject);
        entityManager.persist(uni);
        entityManager.persist(user);
        entityManager.persist(content);
        entityManager.persist(uniSubject);
        entityManager.persist(thread);
    }


    @Test
    public void  userPersist() throws Exception{
        User user1 = this.userRepository.findByEmail(user.getEmail());
        Assert.assertEquals(user, user1);
    }

    @Test
    public void userEnroll() throws Exception{
        user.setUniversity(uni);
        Assert.assertEquals(userRepository.findOne(user.getId()).getUniversity(), uni);
        Assert.assertTrue(universityRepository.findOne(uni.getId()).getUsers().contains(user));
    }


    @Test
    public void userCreateThread() throws Exception{


        Assert.assertEquals(threadRepository.findOne(thread.getId()), thread);
        Assert.assertEquals(threadRepository.findOne(thread.getId()).getCreator(), user);
        Assert.assertTrue(userRepository.findOne(user.getId()).getCreatedPosts().contains(thread));
    }

    @Test
    public void upvote() throws Exception{

        long upvotes = thread.getUpVotes();
        long karma = user.getKarma();
        threadRepository.findOne(thread.getId()).upvote(userRepository.findOne(user.getId()));
        Assert.assertEquals(threadRepository.findOne(thread.getId()).getUpVotes(), upvotes+1);
        Assert.assertEquals(userRepository.findOne(user.getId()).getKarma(), karma+1);
        threadRepository.findOne(thread.getId()).upvote(userRepository.findOne(user.getId()));
        Assert.assertEquals(threadRepository.findOne(thread.getId()).getUpVotes(), upvotes);
        Assert.assertEquals(userRepository.findOne(user.getId()).getKarma(), karma);
    }


    @Test (expected = PersistenceException.class)
    public void usernameDuplication() throws Exception {
            entityManager.persist(new User.UserBuilder().email(email).username(username).password(password).build());
    }

    @Test
    public void userComment() throws Exception{

    }
}
