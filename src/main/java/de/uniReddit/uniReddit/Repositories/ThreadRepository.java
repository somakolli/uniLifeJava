package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.UniThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sokol Makolli
 */
@Repository
public interface ThreadRepository extends JpaRepository<UniThread, Long> {
}
