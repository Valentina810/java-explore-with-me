package ru.practicum.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.State;

import java.util.Set;

public interface StateRepository extends JpaRepository<State, Long> {
	State findByName(String status);

	@Query(value = "select new ru.practicum.event.model.State(st.id,st.name) " +
			"from State as st  " +
			"where st.name in (:names) ")
	Set<State> findByNames(Set<String> names);
}