package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.UTUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Sokol Makolli
 */
@Repository
public interface UserRepository extends JpaRepository<UTUser, Long> {
    UTUser findByEmail(String email);
    UTUser findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
