package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.UniSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniSubjectRepository extends JpaRepository<UniSubject, Long> {
    boolean existsById(Long id);
    List<UniSubject> findAllByUniversityId(Long id);
}
