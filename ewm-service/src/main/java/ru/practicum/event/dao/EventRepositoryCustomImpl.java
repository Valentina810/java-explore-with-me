package ru.practicum.event.dao;

import org.springframework.stereotype.Component;
import ru.practicum.event.model.Event;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class EventRepositoryCustomImpl implements EventRepositoryCustom {
	private final EntityManager entityManager;

	public EventRepositoryCustomImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<Event> searchByCriteria(Set<Long> users, Set<Long> states,
	                                    Set<Long> categories, LocalDateTime rangeStart,
	                                    LocalDateTime rangeEnd, Integer from, Integer size) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Event> cr = cb.createQuery(Event.class);
		Root<Event> root = cr.from(Event.class);
		root.fetch("state", JoinType.LEFT);
		root.fetch("category", JoinType.LEFT);
		root.fetch("location", JoinType.LEFT);
		root.fetch("initiator", JoinType.LEFT);
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
		return entityManager.createQuery(cr).setFirstResult(from).setMaxResults(size).getResultList();
	}

	@Override
	public List<Event> searchByCriteria(String text, Set<Long> categories, Boolean paid,
	                                    LocalDateTime rangeStart,
	                                    LocalDateTime rangeEnd,
	                                    Boolean onlyAvailable,
	                                    String sort,
	                                    Long state,
	                                    Integer from, Integer size) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Event> cr = cb.createQuery(Event.class);
		Root<Event> root = cr.from(Event.class);
		root.fetch("state", JoinType.LEFT);
		root.fetch("category", JoinType.LEFT);
		root.fetch("location", JoinType.LEFT);
		root.fetch("initiator", JoinType.LEFT);
		Predicate criteria = cb.conjunction();
		if (text != null && !text.isBlank()) {
			criteria = cb.and(criteria, cb.or(
					cb.like(cb.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
					cb.like(cb.lower(root.get("description")), "%" + text.toLowerCase() + "%")));
		}
		if (categories != null && !categories.isEmpty()) {
			criteria = cb.and(criteria, root.get("category").in(categories));
		}
		if (paid != null) {
			criteria = cb.and(criteria, root.get("paid").in(paid));
		}
		if ((rangeStart == null) && (rangeEnd == null)) {
			criteria = cb.and(criteria,
					cb.greaterThanOrEqualTo(root.get("eventDate"),
							LocalDateTime.now()));
		} else {
			if (rangeStart != null) {
				criteria = cb.and(criteria, cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
			}
			if (rangeEnd != null) {
				criteria = cb.and(criteria, cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
			}
		}
		if ((onlyAvailable != null) && (onlyAvailable.equals(true))) {
			criteria = cb.and(criteria, cb.gt(root.get("participantLimit"), root.get("confirmedRequests")));
		}
		criteria = cb.and(criteria, root.get("state").in(state));
		cr.select(root).where(criteria);
		if ("EVENT_DATE".equals(sort)) {
			cr.orderBy(cb.asc(root.get("eventDate")));
		} else if ("VIEWS".equals(sort)) {
			cr.orderBy(cb.asc(root.get("views")));
		}

		return entityManager.createQuery(cr).setFirstResult(from).setMaxResults(size).getResultList();
	}
}