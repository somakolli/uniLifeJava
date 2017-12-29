package de.uniReddit.uniReddit.Repositories;

import com.sun.xml.internal.bind.v2.model.core.ID;
import de.uniReddit.uniReddit.Models.UTUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Sokol Makolli
 */
@Repository
public interface UserRepository extends JpaRepository<UTUser, UUID> {
    UTUser findByEmail(String email);
    UTUser findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Query("SELECT sum(upvotes) FROM Post where creator_id = ?1 group by creator_id")
    long countKarma(UUID userId);

}
