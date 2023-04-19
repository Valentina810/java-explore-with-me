package ru.practicum.comment.model;

import lombok.*;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "comments", schema = "public")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_comment", nullable = false, unique = true)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	@Column(name = "text_comment", nullable = false)
	private String text;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "state_comments_id", nullable = false)
	private StateComment stateComment;

	@Column(name = "created")
	private LocalDateTime created;
}