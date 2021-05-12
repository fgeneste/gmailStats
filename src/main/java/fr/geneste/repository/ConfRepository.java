package fr.geneste.repository;

import fr.geneste.domain.Conf;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Conf entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfRepository extends JpaRepository<Conf, Long> {
    Conf findByKey(String key);
}
