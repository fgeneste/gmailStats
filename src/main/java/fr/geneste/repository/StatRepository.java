package fr.geneste.repository;

import fr.geneste.domain.Stat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Stat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {}
