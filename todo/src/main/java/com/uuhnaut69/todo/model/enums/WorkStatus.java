package com.uuhnaut69.todo.model.enums;

public enum WorkStatus {

	PLANNING("Planning"), DOING("Doing"), COMPLETE("Complete");

	private String value;

	WorkStatus(String value) {
		this.value = value;
	}

}
