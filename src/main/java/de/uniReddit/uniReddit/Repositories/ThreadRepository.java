package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.UniSubject;
import de.uniReddit.uniReddit.Models.UniThread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author Sokol Makolli
 */
@Repository
public interface ThreadRepository extends JpaRepository<UniThread, Long> {
    Page<UniThread> findAllByUniSubject(UniSubject uniSubject, Pageable pageable);
}
