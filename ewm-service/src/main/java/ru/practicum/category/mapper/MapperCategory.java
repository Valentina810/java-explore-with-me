package ru.practicum.category.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryCreateDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperCategory {
	public static CategoryDto toCategoryDto(Category category) {
		return CategoryDto.builder()
				.id(category.getId())
				.name(category.getName())
				.build();
	}

	public static Category toCategory(CategoryCreateDto categoryCreateDto) {
		return Category.builder()
				.name(categoryCreateDto.getName()).build();
	}
}