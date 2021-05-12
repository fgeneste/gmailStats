package fr.geneste.repository;

import fr.geneste.domain.Count;
import fr.geneste.domain.Message;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data SQL repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByDateAndAndFrom(Instant date, String from);

    @Query(
        value = "SELECT lower(jhi_from) as label, count(*) as val FROM MESSAGE WHERE account=?1 GROUP BY lower(jhi_from)",
        nativeQuery = true)
    List<Count> findCountByFrom(String account);

    @Query(
        value = "SELECT count(*) FROM MESSAGE WHERE account=?1 and (corps is null OR corps='')",
        nativeQuery = true)
    String findCountOfVoids(String account);

    @Query(
        value = "SELECT count(*) FROM MESSAGE WHERE account=?1 ",
        nativeQuery = true)
    String findCount(String account);
}
