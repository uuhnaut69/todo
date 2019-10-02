package com.uuhnaut69.todo.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@CreatedBy
	@Column(nullable = false)
	private String createdBy;

	@LastModifiedBy
	@Column(nullable = false)
	private String updatedBy;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedDate;

}
