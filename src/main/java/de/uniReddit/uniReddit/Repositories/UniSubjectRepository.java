package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.UniSubject;
import de.uniReddit.uniReddit.Models.University;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UniSubjectRepository extends JpaRepository<UniSubject, Long> {
    List<UniSubject> findAllByUniversity(University university, Pageable pageable);
    UniSubject findByUniversityAndName(University university, String name);
}
