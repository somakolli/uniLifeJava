package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.Post;
import de.uniReddit.uniReddit.Models.UniSubject;
import de.uniReddit.uniReddit.Models.UniThread;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Sokol Makolli
 */
@Repository
public interface ThreadRepository extends JpaRepository<UniThread, Long> {
    List<UniThread> findAllByUniSubject(UniSubject uniSubject, Pageable pageable);
    @Query("SELECT u from UniThread u WHERE u.uniSubject = ?1 ORDER BY (?2-u.created)*u.upvotes ASC")
    List<UniThread> findAndSortByTrending(UniSubject uniSubject,int currentTime, Pageable pageable);

    List<UniThread> findAllByUniSubjectIn(List<UniSubject> uniSubjects, Pageable pageable);

    @Query("SELECT u from UniThread u where u.uniSubject IN ?1 ORDER BY ?2 - u.created + upvotes ASC")
    List<UniThread> findMultipleAndSortByTrending(List<UniSubject> subjects, Long currentTime);
}
