package it.epicode.gestione_eventi.repository;

import it.epicode.gestione_eventi.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
