package ru.practicum.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(path = "/categories")
public class PublicCategoryController {
	private final CategoryService categoryService;

	@Autowired
	public PublicCategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
	                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		return categoryService.getCategories(from, size);
	}

	@GetMapping("/{catId}")
	public CategoryDto getCategory(@PathVariable long catId) {
		return categoryService.getCategory(catId);
	}
}