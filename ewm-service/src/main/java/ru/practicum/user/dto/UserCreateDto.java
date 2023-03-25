package ru.practicum.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserCreateDto {
	@NotNull
	@NotBlank
	private String name;
	@Email
	@NotNull
	@NotBlank
	private String email;
}