package fr.geneste.repository;

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
}
