package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.StatCreateDto;
import ru.practicum.dto.StatDto;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperStat {

	public static Stat toStat(StatCreateDto statCreateDto) {

		return Stat.builder()
				.app(statCreateDto.getApp())
				.uri(statCreateDto.getUri())
				.ip(statCreateDto.getIp())
				.timestamp(stringToLocalDateTime(statCreateDto.getTimestamp()))
				.build();
	}

	public static StatDto toStatDto(Stat stat) {
		return StatDto.builder()
				.id(stat.getId())
				.app(stat.getApp())
				.uri(stat.getUri())
				.ip(stat.getIp())
				.timestamp(localDateTimeToString(stat.getTimestamp()))
				.build();
	}

	public static LocalDateTime stringToLocalDateTime(String date) {
		return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public static String localDateTimeToString(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}