package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Stat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	@Column(name = "app", nullable = false)
	private String app;
	@Column(name = "uri", nullable = false)
	private String uri;
	@Column(name = "ip", nullable = false)
	private String ip;
	@Column(name = "timestamp")
	private LocalDateTime timestamp;
}
