package com.uuhnaut69.todo.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uuhnaut69.todo.exception.AppException;
import com.uuhnaut69.todo.exception.BadRequestException;
import com.uuhnaut69.todo.exception.ResourceNotFoundException;
import com.uuhnaut69.todo.model.entity.Work;
import com.uuhnaut69.todo.model.payload.WorkRequest;
import com.uuhnaut69.todo.repository.WorkRepository;
import com.uuhnaut69.todo.service.WorkService;
import com.uuhnaut69.todo.ulti.AppUltis;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class WorkServiceImpl implements WorkService {

	private final WorkRepository workRepository;

	public WorkServiceImpl(WorkRepository workRepository) {
		this.workRepository = workRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Work> findAll(Pageable pageable) {
		log.info("findAll work query from db");
		return workRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Work findById(Long id) {
		log.info("findById query from db, id: {}", id);
		Optional<Work> work = workRepository.findById(id);
		return work.orElseThrow(() -> new ResourceNotFoundException("Not found work " + id));
	}

	@Override
	public Work add(WorkRequest req) throws AppException {
		checkWorkName(req.getWorkName());
		log.debug("add work to db, work: {}", req.toString());
		return save(req, new Work());
	}

	@Override
	public Work edit(Long id, WorkRequest req) throws AppException {
		Work work = findById(id);
		checkWorkName(req.getWorkName());
		log.debug("update work to db, work: {}", req.toString());
		return save(req, work);
	}

	@Override
	public void delete(Long id) throws AppException {
		Work work = findById(id);
		log.debug("remove work from db, id: {}", id);
		workRepository.delete(work);
	}

	@Override
	public void checkWorkName(String workName) {
		if (workRepository.existsByWorkName(workName)) {
			throw new BadRequestException(workName + " already exists");
		}
	}

	private Work save(WorkRequest req, Work work) {
		AppUltis.convertToEntity(req, work);
		return workRepository.save(work);
	}

}
