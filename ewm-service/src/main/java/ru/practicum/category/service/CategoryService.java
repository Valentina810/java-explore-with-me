package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryCreateDto;
import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
	CategoryDto addCategory(CategoryCreateDto categoryCreateDto);

	void deleteCategory(long catId);

	CategoryDto updateCategory(long catId, CategoryCreateDto categoryCreateDto);

	List<CategoryDto> getCategories(Integer from, Integer size);

	CategoryDto getCategory(long catId);
}