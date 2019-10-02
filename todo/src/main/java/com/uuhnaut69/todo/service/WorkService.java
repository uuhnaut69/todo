package com.uuhnaut69.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uuhnaut69.todo.exception.AppException;
import com.uuhnaut69.todo.model.entity.Work;
import com.uuhnaut69.todo.model.payload.WorkReq;

public interface WorkService {

	Page<Work> findAll(Pageable pageable);

	Work findById(Long id);

	Work add(WorkReq req) throws AppException;

	Work edit(Long id, WorkReq req) throws AppException;

	void delete(Long id) throws AppException;

	void checkWorkName(String workName);
}
