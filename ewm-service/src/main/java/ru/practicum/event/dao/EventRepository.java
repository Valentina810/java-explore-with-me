package ru.practicum.event.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

	@Query(value = "from Event as ev " +
			"join fetch ev.initiator init " +
			"join fetch ev.state " +
			"join fetch ev.location " +
			"join fetch ev.category " +
			"where init.id = :userId ")
	List<Event> findByInitiatorId(long userId, Pageable pageable);

	@Query(value = "from Event as ev " +
			"join fetch ev.initiator " +
			"join fetch ev.state " +
			"join fetch ev.location " +
			"join fetch ev.category " +
			"where ev.id = :eventId ")
	Optional<Event> findById(long eventId);

	@Query(value = "from Event as ev " +
			"join fetch ev.initiator " +
			"join fetch ev.state " +
			"join fetch ev.location " +
			"join fetch ev.category " +
			"where ev.id in (:ids) ")
	List<Event> getEvents(List<Long> ids);
}