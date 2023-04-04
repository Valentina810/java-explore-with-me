package ru.practicum.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
	Optional<Request> findByEventIdAndRequesterId(Long eventId, Long requesterId);

	List<Request> findByRequesterId(long userId);

	List<Request> findByEventId(long eventId);

	@Query(value = "select new ru.practicum.request.model.Request(rq.id,rq.event,rq.requester,rq.created,rq.status) " +
			"from Request as rq " +
			"where rq.id in (:ids) ")
	List<Request> getRequests(List<Long> ids);
}