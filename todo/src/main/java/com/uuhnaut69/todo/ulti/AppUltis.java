package com.uuhnaut69.todo.ulti;

import org.springframework.beans.BeanUtils;

import com.uuhnaut69.todo.model.entity.AbstractEntity;

public class AppUltis {

	public static <T extends AbstractEntity> T convertToEntity(Object src, T dest, String... ignoreProperties) {
		BeanUtils.copyProperties(src, dest, ignoreProperties);
		return dest;
	}

}
