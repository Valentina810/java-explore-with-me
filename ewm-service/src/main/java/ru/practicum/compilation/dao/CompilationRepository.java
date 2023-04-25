package ru.practicum.compilation.dao;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.compilation.model.Compilation;

import java.util.List;
import java.util.Optional;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
	@Query(value = "from Compilation cm "+
			"left join fetch cm.events ev " +
			"left join fetch ev.initiator " +
			"left join fetch ev.state " +
			"left join fetch ev.location " +
			"left join fetch ev.category " +
			"where cm.pinned=:pinned")
	List<Compilation> findByPinned(boolean pinned, PageRequest pageRequest);

	@Query(value = "from Compilation cm "+
			"left join fetch cm.events ev " +
			"left join fetch ev.initiator " +
			"left join fetch ev.state " +
			"left join fetch ev.location " +
			"left join fetch ev.category ")
	List<Compilation> findAll(PageRequest pageRequest);

	@Query(value = "from Compilation cm "+
			"left join fetch cm.events ev " +
			"left join fetch ev.initiator " +
			"left join fetch ev.state " +
			"left join fetch ev.location " +
			"left join fetch ev.category "+
			"where cm.id=:compId")
	Optional<Compilation> findById(long compId);
}