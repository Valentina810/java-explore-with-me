package ru.practicum.category.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import ru.practicum.category.dao.CategoryRepository;
import ru.practicum.category.dto.CategoryCreateDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.MapperCategory;
import ru.practicum.category.model.Category;
import ru.practicum.exception.NotFoundException;

@Service
@Log
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public CategoryDto addCategory(CategoryCreateDto categoryCreateDto) {
		CategoryDto categoryDto = MapperCategory.toCategoryDto(categoryRepository
				.save(MapperCategory.toCategory(categoryCreateDto)));
		log.info("Добавлена новая категория " + categoryDto);
		return categoryDto;
	}

	@Override
	public void deleteCategory(long catId) {
		categoryRepository.delete(categoryRepository.findById(catId).orElseThrow(() ->
				new NotFoundException(String.format("Возникла ошибка при удалении категории с id %d", catId))));
		log.info(String.format("Удалена категория с id %d", catId));
	}

	@Override
	public CategoryDto updateCategory(long catId, CategoryCreateDto categoryCreateDto) {
		Category category = categoryRepository.findById(catId).orElseThrow(() ->
				new NotFoundException(String.format("Категория с id %d не найдена", catId)));
		category.setName(categoryCreateDto.getName());
		CategoryDto categoryDto = MapperCategory.toCategoryDto(categoryRepository.save(category));
		log.info("Изменена категория" + categoryDto);
		return categoryDto;
	}
}
