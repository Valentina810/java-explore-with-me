package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryCreateDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@Validated
public class AdminCategoryController {
	private final CategoryService categoryService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryDto addCategory(@Valid @RequestBody CategoryCreateDto categoryCreateDto) {
		return categoryService.addCategory(categoryCreateDto);
	}

	@DeleteMapping("/{catId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable long catId) {
		categoryService.deleteCategory(catId);
	}

	@PatchMapping("/{catId}")
	@ResponseStatus(HttpStatus.OK)
	public CategoryDto updateCategory(@PathVariable long catId,
	                                  @Valid @RequestBody CategoryCreateDto categoryCreateDto) {
		return categoryService.updateCategory(catId, categoryCreateDto);
	}
}