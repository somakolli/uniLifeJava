package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.Post;
import de.uniReddit.uniReddit.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Long countAllByCreatorEquals(User user);

}
