package ru.practicum.compilation.dao;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
	@Query(value = "from Compilation cm " +
			"join fetch cm.events ev " +
			"where cm.pinned=:pinned")
	List<Compilation> findByPinned(boolean pinned, PageRequest pageRequest);
}