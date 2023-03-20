package ru.practicum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Stat;
import ru.practicum.model.ViewStat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


public interface StatsRepository extends JpaRepository<Stat, Long> {
	@Query(value = "select new ru.practicum.model.ViewStat(st.app,st.uri,count(distinct st.ip)) " +
			"from Stat st "+
			"where st.timestamp between :start and :end " +
			"and st.uri in (:uris) " +
			"group by st.app,st.uri "+
			"order by count(distinct st.ip) desc "
	)
	List<ViewStat> getStatsUniqueTrue(LocalDateTime start, LocalDateTime end, Set<String> uris);

	@Query(value = "select new ru.practicum.model.ViewStat(st.app,st.uri,count(st.ip)) " +
			"from Stat st "+
			"where st.timestamp between :start and :end " +
			"and st.uri in (:uris) " +
			"group by st.app,st.uri "+
			"order by count(st.ip) desc "
	)
	List<ViewStat> getStatsUniqueFalse(LocalDateTime start, LocalDateTime end, Set<String> uris);
}
