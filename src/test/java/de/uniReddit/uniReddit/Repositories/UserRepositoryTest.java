package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE )
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private ThreadRepository threadRepository;

    private static final String email = "s.makolli@aol.de";
    private static final String username = "sokol";

    @Test
    public void  userPersist() throws Exception{
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        this.entityManager.persist(user);
        User user1 = this.repository.findByEmail(user.getEmail());
        Assert.assertEquals(user, user1);
    }

    @Test
    public void userEnroll() throws Exception{
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        University uni = new University();
        uni.setLocation("Stuttgart");
        uni.setName("Uni Stuttgart");
        entityManager.persist(user);
        entityManager.persist(uni);
        user.setUniversity(uni);
        Assert.assertEquals(repository.findOne(user.getId()).getUniversity(), uni);
        Assert.assertTrue(universityRepository.findOne(uni.getId()).getUsers().contains(user));
    }

    @Test
    public void userCreateThread() throws Exception{
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        University uni = new University();
        uni.setLocation("Stuttgart");
        uni.setName("Uni Stuttgart");
        PostContent content = new PostContent("Hallo");
        UniSubject uniSubject = new UniSubject(uni);
        UniThread thread = new UniThread(content,user, "Test",  uniSubject);
        entityManager.persist(uni);
        entityManager.persist(user);
        entityManager.persist(content);
        entityManager.persist(uniSubject);
        entityManager.persist(thread);
        Assert.assertEquals(threadRepository.findOne(thread.getId()), thread);
        Assert.assertEquals(threadRepository.findOne(thread.getId()).getCreator(), user);
        Assert.assertTrue(repository.findOne(user.getId()).getCreatedPosts().contains(thread));

    }
}
