package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.UniSubject;
import de.uniReddit.uniReddit.Models.UniThread;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Sokol Makolli
 */
@Repository
public interface ThreadRepository extends JpaRepository<UniThread, UUID> {
    List<UniThread> findAllByUniSubject(UniSubject uniSubject, Pageable pageable);
}
