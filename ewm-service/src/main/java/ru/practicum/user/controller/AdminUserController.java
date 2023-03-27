package ru.practicum.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
public class AdminUserController {
	private final UserService userService;

	@Autowired
	public AdminUserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<UserDto> getAllUsers(@RequestParam(name = "ids") Set<Long> ids,
	                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
	                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		return userService.getAllUsers(ids,from,size);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto addUser(@Valid @RequestBody UserCreateDto userCreateDto) {
		return userService.addUser(userCreateDto);
	}

	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable long userId) {
		userService.deleteUser(userId);
	}
}