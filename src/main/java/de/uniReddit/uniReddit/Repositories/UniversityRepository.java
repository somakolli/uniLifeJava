package de.uniReddit.uniReddit.Repositories;

import de.uniReddit.uniReddit.Models.University;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UniversityRepository extends PagingAndSortingRepository<University, Long> {
}
