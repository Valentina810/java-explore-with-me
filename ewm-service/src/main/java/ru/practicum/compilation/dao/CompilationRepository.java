package ru.practicum.compilation.dao;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
	List<Compilation> findByPinned(boolean pinned, PageRequest pageRequest);
}