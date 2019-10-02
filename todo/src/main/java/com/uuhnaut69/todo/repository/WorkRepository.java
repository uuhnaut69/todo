package com.uuhnaut69.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uuhnaut69.todo.model.entity.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

	Page<Work> findAll(Pageable pageable);

	Boolean existsByWorkName(String workName);

}
