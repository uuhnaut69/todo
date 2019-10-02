package com.uuhnaut69.todo.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uuhnaut69.todo.model.entity.Work;
import com.uuhnaut69.todo.model.payload.WorkReq;
import com.uuhnaut69.todo.service.WorkService;

@RestController
@RequestMapping("/v1/api/works")
public class WorkController {

	private final WorkService workService;

	public WorkController(WorkService workService) {
		this.workService = workService;
	}

	@GetMapping
	public ResponseEntity<Page<Work>> findAll(@RequestParam(value = "sort", defaultValue = "id") String sortBy,
			@RequestParam(value = "order", defaultValue = "desc") String order,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		Sort sort = "asc".equals(order.toLowerCase()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Page<Work> works = workService.findAll(PageRequest.of(page, pageSize, sort));
		return new ResponseEntity<Page<Work>>(works, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Work> findById(@PathVariable Long id) {
		Work work = workService.findById(id);
		return new ResponseEntity<Work>(work, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Work> add(@RequestBody @Valid WorkReq req) {
		Work work = workService.add(req);
		return new ResponseEntity<Work>(work, HttpStatus.CREATED);
	}

	@PutMapping("/{id}/edit")
	public ResponseEntity<Work> edit(@PathVariable Long id, @RequestBody @Valid WorkReq req) {
		Work work = workService.edit(id, req);
		return new ResponseEntity<Work>(work, HttpStatus.OK);
	}

	@DeleteMapping("/{id}/delete")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		workService.delete(id);
		return new ResponseEntity<String>("Delete successfully", HttpStatus.OK);
	}
}
