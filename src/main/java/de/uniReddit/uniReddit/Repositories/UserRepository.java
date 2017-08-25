package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;

/**
 * @author Sokol Makolli
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);

}
