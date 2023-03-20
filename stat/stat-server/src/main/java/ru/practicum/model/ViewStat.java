package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ViewStat {
	String app;
	String uri;
	Long hits;
}