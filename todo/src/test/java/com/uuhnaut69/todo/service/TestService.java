package com.uuhnaut69.todo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.uuhnaut69.todo.exception.BadRequestException;
import com.uuhnaut69.todo.exception.ResourceNotFoundException;
import com.uuhnaut69.todo.model.entity.Work;
import com.uuhnaut69.todo.model.enums.WorkStatus;
import com.uuhnaut69.todo.model.payload.WorkRequest;
import com.uuhnaut69.todo.repository.WorkRepository;
import com.uuhnaut69.todo.service.impl.WorkServiceImpl;
import com.uuhnaut69.todo.ulti.AppUltis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestService {

	@Mock
	private WorkRepository workRepository;

	@InjectMocks
	private WorkServiceImpl workService;

	@Test
	public void test_checkWorkName_valid() {

		when(workRepository.existsByWorkName(any())).thenReturn(false);
		workService.checkWorkName("Test 1");
		verify(workRepository, times(1)).existsByWorkName(any());
	}

	@Test
	public void test_checkWorkName_inValid() {
		when(workRepository.existsByWorkName(any())).thenReturn(true);
		try {
			workService.checkWorkName("Test 1");
		} catch (Exception e) {
			assertTrue(e instanceof BadRequestException);
			assertEquals("Test 1 already exists", e.getMessage());
		}
	}

	@Test
	public void test_delete_success() throws Exception {
		Work work = new Work();
		work.setWorkName("Test 1");
		work.setWorkStatus(WorkStatus.PLANNING);

		when(workRepository.findById(1L)).thenReturn(Optional.of(work));

		workService.delete(1L);

		verify(workRepository, times(1)).delete(any());
	}

	@Test
	public void test_delete_dataNotFound() {
		when(workRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
		try {
			workService.delete(1L);
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
			assertEquals("Work not found with work id : 1", e.getMessage());
		}
	}

	@Test
	public void test_add_success() throws Exception {

		WorkRequest workRequest = new WorkRequest();
		workRequest.setWorkName("Test 1");
		workRequest.setWorkStatus(WorkStatus.PLANNING);

		when(workRepository.save(any())).thenReturn(AppUltis.convertToEntity(workRequest, new Work()));
		Work work = workService.add(workRequest);

		assertEquals("Test 1", work.getWorkName());
		assertEquals(WorkStatus.PLANNING, work.getWorkStatus());
	}

	@Test
	public void test_add_failed() {
		WorkRequest workRequest = new WorkRequest();
		workRequest.setWorkName("Test 1");
		workRequest.setWorkStatus(WorkStatus.PLANNING);

		when(workRepository.existsByWorkName(any())).thenReturn(true);
		when(workRepository.save(any())).thenReturn(AppUltis.convertToEntity(workRequest, new Work()));

		try {
			workService.add(workRequest);
		} catch (Exception e) {
			assertTrue(e instanceof BadRequestException);
			assertEquals("Test 1 already exists", e.getMessage());
		}
	}

	@Test
	public void test_edit_success() throws Exception {

		WorkRequest workRequest = new WorkRequest();
		workRequest.setWorkName("Test 1");
		workRequest.setWorkStatus(WorkStatus.PLANNING);
		Work work = AppUltis.convertToEntity(workRequest, new Work());
		when(workRepository.findById(any())).thenReturn(Optional.of(work));
		when(workRepository.save(any())).thenReturn(work);

		Work result = workService.edit(1L, workRequest);
		assertEquals("Test 1", result.getWorkName());
		assertEquals(WorkStatus.PLANNING, result.getWorkStatus());
	}

	@Test
	public void test_edit_failed_by_notfound() {
		WorkRequest workRequest = new WorkRequest();
		workRequest.setWorkName("Test 1");
		workRequest.setWorkStatus(WorkStatus.PLANNING);

		when(workRepository.findById(any())).thenReturn(Optional.ofNullable(null));

		try {
			workService.edit(1L, workRequest);
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
			assertEquals("Work not found with work id : 1", e.getMessage());
		}
	}

	@Test
	public void test_edit_failed_by_workNameExist() {
		WorkRequest workRequest = new WorkRequest();
		workRequest.setWorkName("Test 1");
		workRequest.setWorkStatus(WorkStatus.PLANNING);

		Work work = new Work();
		work.setWorkName("Test 1");
		work.setWorkStatus(WorkStatus.PLANNING);

		when(workRepository.findById(any())).thenReturn(Optional.of(work));
		when(workRepository.existsByWorkName(any())).thenReturn(true);
		when(workRepository.save(any())).thenReturn(work);

		try {
			workService.edit(1L, workRequest);
		} catch (Exception e) {
			assertTrue(e instanceof BadRequestException);
			assertEquals("Test 1 already exists", e.getMessage());
		}
	}
}
