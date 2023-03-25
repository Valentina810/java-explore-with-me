package ru.practicum.user.service;

import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dao.UserRepository;
import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.MapperUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Log
public class UserServiceImpl {
	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public UserDto addUser(UserCreateDto userCreateDto) {
		UserDto userDto = MapperUser.toUserDto(userRepository.save(MapperUser.toUser(userCreateDto)));
		log.info("Добавлен новый пользователь " + userDto);
		return userDto;
	}

	@Transactional(readOnly = true)
	public List<UserDto> getAllUsers(Set<Long> ids, Integer from, Integer size) {
		List<UserDto> userDtos = new ArrayList<>();
		userRepository.getUsers(ids, PageRequest.of(from / size, size))
				.forEach(e -> userDtos.add(MapperUser.toUserDto(e)));
		log.info("Получен список пользователей " + userDtos);
		return userDtos;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void deleteUser(long userId) {
		userRepository.delete(userRepository.findById(userId).orElseThrow(() ->
				new NotFoundException(String.format("Возникла ошибка при удалении пользователя с id %d", userId))));
		log.info(String.format("Удален пользователь с id %d", userId));
	}
}