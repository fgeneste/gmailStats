package fr.geneste.repository;

import fr.geneste.domain.Attachement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Attachement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachementRepository extends JpaRepository<Attachement, Long> {}
