package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.UniSubject;
import de.uniReddit.uniReddit.Models.UniThread;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ThreadRepositoryTest {
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private UniSubjectRepository uniSubjectRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    public void findMultiple(){
        UniSubject uniSubject1 = new UniSubject();
        uniSubjectRepository.save(uniSubject1);
        UniSubject uniSubject2 = new UniSubject();
        uniSubjectRepository.save(uniSubject2);
        int[] created = {-0, -10, -100, -1000, -10000};
        int[] upvotes = {1,1,1,1,1};
        createThreads(created, upvotes, uniSubject1);
        createThreads(created, upvotes, uniSubject2);
        List<UniSubject> uniSubjects = uniSubjectRepository.findAll();
        Long time = System.currentTimeMillis();
        List<UniThread> threads = threadRepository.findAllByUniSubjectIn(uniSubjects, new PageRequest(0, 100, Sort.Direction.ASC, "created"));
        Assert.assertTrue(threads.size()==10);
    }

    @Test
    public void testFindAndSortByTrending(){
        UniSubject uniSubject = new UniSubject();
        uniSubjectRepository.save(uniSubject);
        int[] created = {-0, -10, -100, -1000, -10000};
        int[] upvotes = {1,1,1,1,1};
        createThreads(created, upvotes, uniSubject);
        List<UniThread> threads = threadRepository.findAndSortByTrending(uniSubject,(int)(System.currentTimeMillis()/1000), new PageRequest(0,100));

    }


    public void createThreads(int[] created, int[] upvotes, UniSubject uniSubject){
        for (int i = 0; i < upvotes.length; i++) {
            UniThread thread = new UniThread();
            thread.setCreated((int) (System.currentTimeMillis() / 1000) + created[i]);
            float e = 5f/7f;
            float f = ((float)thread.getCreated()/(float)(System.currentTimeMillis()/1000f));
            System.out.println("f:" + f);
            thread.setUpvotes(upvotes[i]);
            thread.setUniSubject(uniSubject);
            threadRepository.save(thread);
        }
    }
}
