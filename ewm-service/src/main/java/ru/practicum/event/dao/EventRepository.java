package ru.practicum.event.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findByInitiatorId(long userId, Pageable pageable);

	Optional<Event> findById(long eventId);

	@Query(value = "from Event as ev " +
			"where ev.id in (:ids) ")
	List<Event> getEvents(List<Long> ids);
}