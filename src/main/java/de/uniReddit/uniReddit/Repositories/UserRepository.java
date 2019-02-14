package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.UtUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Sokol Makolli
 */
@Repository
public interface UserRepository extends JpaRepository<UtUser, Long> {
    UtUser findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT sum(upvotes) FROM Post where creator_id = ?1 group by creator_id")
    long countKarma(Long userId);
    UtUser findByUid(String uid);
    boolean existsByUid(String uid);

}
