package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.PostContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostContentRepository extends JpaRepository<PostContent, Long> {
}
