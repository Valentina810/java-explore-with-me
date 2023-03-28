package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "locations", schema = "public")
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_location", nullable = false, unique = true)
	private Long id;
	@Column(name = "lat", nullable = false)
	private Float lat;

	@Column(name = "lon", nullable = false)
	private Float lon;
}