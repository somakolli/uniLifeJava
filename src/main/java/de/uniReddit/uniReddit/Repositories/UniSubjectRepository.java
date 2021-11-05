package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.UniSubject;
import de.uniReddit.uniReddit.Models.University;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UniSubjectRepository extends JpaRepository<UniSubject, Long> {
    List<UniSubject> findAllByUniversity(University university, Pageable pageable);
    UniSubject findByUniversityAndName(University university, String name);
    @Transactional
    @Modifying
    @Query("UPDATE UniSubject s set s.numberOfSubscribers = s.numberOfSubscribers + 1 WHERE s.id = ?1")
    void incrementVotesById(Long uniSubjectId);
    @Transactional
    @Modifying
    @Query("UPDATE UniSubject s set s.numberOfSubscribers = s.numberOfSubscribers - 1 WHERE s.id = ?1")
    void decrementVotesById(Long uniSubjectId);
}
