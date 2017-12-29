package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.Comment;
import de.uniReddit.uniReddit.Models.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Sokol Makolli
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByParent(Post parent, Pageable pageable);
}
