package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sokol Makolli
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByParentId(Long parentId, Pageable pageable);
}
