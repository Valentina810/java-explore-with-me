package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryDto {
	private Long id;
	private String name;
}