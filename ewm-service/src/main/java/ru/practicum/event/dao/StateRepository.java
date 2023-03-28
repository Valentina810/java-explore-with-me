package ru.practicum.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.State;

public interface StateRepository extends JpaRepository<State, Long> {
	State findByName(String status);
}