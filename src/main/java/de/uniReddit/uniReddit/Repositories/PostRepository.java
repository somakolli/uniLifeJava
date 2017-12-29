package de.uniReddit.uniReddit.Repositories;

import com.sun.xml.internal.bind.v2.model.core.ID;
import de.uniReddit.uniReddit.Models.Post;
import de.uniReddit.uniReddit.Models.UTUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Long countAllByCreatorEquals(UTUser UTUser);

}
