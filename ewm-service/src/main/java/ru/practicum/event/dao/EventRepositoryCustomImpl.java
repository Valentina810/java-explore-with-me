package ru.practicum.event.dao;

import org.springframework.stereotype.Component;
import ru.practicum.event.model.Event;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class EventRepositoryCustomImpl implements EventRepositoryCustom {
	private final EntityManager entityManager;

	public EventRepositoryCustomImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<Event> searchBy(Set<Long> users, Set<Long> states,
	                            Set<Long> categories, LocalDateTime rangeStart,
	                            LocalDateTime rangeEnd, Integer from, Integer size) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Event> cr = cb.createQuery(Event.class);
		Root<Event> root = cr.from(Event.class);
		Predicate criteria = cb.conjunction();
		if (users != null && !users.isEmpty()) {
			criteria = cb.and(criteria, root.get("initiator").in(users));
		}
		if (states != null && !states.isEmpty()) {
			criteria = cb.and(criteria, root.get("state").in(states));
		}
		if (categories != null && !categories.isEmpty()) {
			criteria = cb.and(criteria, root.get("category").in(categories));
		}
		if (rangeStart != null) {
			criteria = cb.and(criteria, cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
		}
		if (rangeEnd != null) {
			criteria = cb.and(criteria, cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
		}
		cr.select(root).where(criteria);
		return entityManager.createQuery(cr).getResultList();
	}
}