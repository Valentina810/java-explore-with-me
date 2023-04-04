package ru.practicum.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperUser {
	public static User toUser(UserCreateDto userCreateDto) {
		return User.builder()
				.name(userCreateDto.getName())
				.email(userCreateDto.getEmail())
				.build();
	}

	public static UserDto toUserDto(User user) {
		return UserDto.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.build();
	}
}