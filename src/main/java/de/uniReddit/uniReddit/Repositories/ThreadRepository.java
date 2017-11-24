package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.UniSubject;
import de.uniReddit.uniReddit.Models.UniThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author Sokol Makolli
 */
@Repository
public interface ThreadRepository extends JpaRepository<UniThread, Long> {
    Collection<UniThread> findAllByUniSubject(UniSubject uniSubject);
}
