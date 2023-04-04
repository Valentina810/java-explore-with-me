package ru.practicum.event.model;

public enum StateEvent {
	PENDING, //В ОЖИДАНИИ
	CONFIRMED, //ПОДТВЕРЖДЕННЫЙ
	REJECTED, //ОТКЛОНЕННЫЙ
	PUBLISHED, //ОПУБЛИКОВАНО
	CANCELED //ОТМЕНЕНО
}