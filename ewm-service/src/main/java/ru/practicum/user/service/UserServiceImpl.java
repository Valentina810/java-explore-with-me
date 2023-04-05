package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dao.UserRepository;
import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.MapperUser;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public UserDto addUser(UserCreateDto userCreateDto) {
		UserDto userDto = MapperUser.toUserDto(userRepository.save(MapperUser.toUser(userCreateDto)));
		log.info("Добавлен новый пользователь {}", userDto);
		return userDto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDto> getAllUsers(Set<Long> ids, Integer from, Integer size) {
		List<UserDto> userDtos = userRepository.getUsers(ids, PageRequest.of(from / size, size))
				.stream().map(MapperUser::toUserDto).collect(Collectors.toList());
		log.info("Получен список пользователей {}", userDtos);
		return userDtos;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void deleteUser(long userId) {
		userRepository.delete(userRepository.findById(userId).orElseThrow(() ->
				new NotFoundException(String.format("Возникла ошибка при удалении пользователя с id %d", userId))));
		log.info("Удален пользователь с id {}", userId);
	}
}