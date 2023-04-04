package ru.practicum.category.service;

import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dao.CategoryRepository;
import ru.practicum.category.dto.CategoryCreateDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.MapperCategory;
import ru.practicum.category.model.Category;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Log
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public CategoryDto addCategory(CategoryCreateDto categoryCreateDto) {
		CategoryDto categoryDto = MapperCategory.toCategoryDto(categoryRepository
				.save(MapperCategory.toCategory(categoryCreateDto)));
		log.info("Добавлена новая категория " + categoryDto);
		return categoryDto;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void deleteCategory(long catId) {
		categoryRepository.delete(categoryRepository.findById(catId).orElseThrow(() ->
				new NotFoundException(String.format("Возникла ошибка при удалении категории с id %d", catId))));
		log.info(String.format("Удалена категория с id %d", catId));
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public CategoryDto updateCategory(long catId, CategoryCreateDto categoryCreateDto) {
		Category category = categoryRepository.findById(catId).orElseThrow(() ->
				new NotFoundException(String.format("Категория с id %d не найдена", catId)));
		category.setName(categoryCreateDto.getName());
		CategoryDto categoryDto = MapperCategory.toCategoryDto(categoryRepository.save(category));
		log.info("Изменена категория" + categoryDto);
		return categoryDto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryDto> getCategories(Integer from, Integer size) {
		List<CategoryDto> categoryDtos = new ArrayList<>();
		categoryRepository.findAll(PageRequest.of(from / size, size))
				.forEach(e -> categoryDtos.add(MapperCategory.toCategoryDto(e)));
		log.info("Получен список категорий " + categoryDtos);
		return categoryDtos;
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryDto getCategory(long catId) {
		return MapperCategory.toCategoryDto(categoryRepository.findById(catId)
				.orElseThrow(() ->
						new NotFoundException(String.format("Категория с id %d не найдена", catId))));

	}
}
