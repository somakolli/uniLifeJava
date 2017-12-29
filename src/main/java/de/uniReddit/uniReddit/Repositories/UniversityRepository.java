package de.uniReddit.uniReddit.Repositories;

import com.sun.xml.internal.bind.v2.model.core.ID;
import de.uniReddit.uniReddit.Models.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Id;
import java.util.UUID;

@Repository
public interface UniversityRepository extends JpaRepository<University, UUID> {
}
