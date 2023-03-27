package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryCreateDto;
import ru.practicum.category.dto.CategoryDto;

public interface CategoryService {
	CategoryDto addCategory(CategoryCreateDto categoryCreateDto);

	void deleteCategory(long catId);

	CategoryDto updateCategory(long catId, CategoryCreateDto categoryCreateDto);
}