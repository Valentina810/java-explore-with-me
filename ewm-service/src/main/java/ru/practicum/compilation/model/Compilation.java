package ru.practicum.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "compilations", schema = "public")
public class Compilation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_compilation", nullable = false, unique = true)
	private Long id;

	@Column(name = "pinned", nullable = false)
	private Boolean pinned;
	@Column(name = "title", nullable = false)
	private String title;

	@ManyToMany
	@JoinTable(name = "events_compilations",
			joinColumns = @JoinColumn(name = "compilation_id", referencedColumnName = "id_compilation"),
			inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id_event"))
	List<Event> events;

	public List<Event> getEvents() {
		if (events == null)
			return Collections.emptyList();
		else return events;
	}
}