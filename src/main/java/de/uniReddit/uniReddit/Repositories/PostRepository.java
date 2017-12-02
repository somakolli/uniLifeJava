package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.Post;
import de.uniReddit.uniReddit.Models.UTUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    Long countAllByCreatorEquals(UTUser UTUser);

}
