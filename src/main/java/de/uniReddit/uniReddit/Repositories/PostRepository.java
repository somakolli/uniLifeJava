package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.Post;
import de.uniReddit.uniReddit.Models.UtUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Long countAllByCreatorEquals(UtUser UTUser);
    @Transactional
    @Modifying
    @Query("UPDATE Post p set p.upvotes = p.upvotes + 1 WHERE p.id = ?1")
    void incrementVotesById(Long postId);
    @Transactional
    @Modifying
    @Query("UPDATE Post p set p.upvotes = p.upvotes - 1 WHERE p.id = ?1")
    void decrementVotesById(Long postId);
}
