package ru.practicum.comment.model;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
@Table(name = "states_comments", schema = "public")
public class StateComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_state_comment", nullable = false, unique = true)
	private Long id;

	@Column(name = "name")
	private String name;
}