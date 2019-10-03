package com.uuhnaut69.todo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.todo.model.entity.Work;
import com.uuhnaut69.todo.model.enums.WorkStatus;
import com.uuhnaut69.todo.model.payload.WorkRequest;
import com.uuhnaut69.todo.service.WorkService;
import com.uuhnaut69.todo.ulti.AppUltis;

@RunWith(SpringRunner.class)
@WebMvcTest(WorkController.class)
public class TestController {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private WorkService workService;

	@Test
	public void test_findById_success() throws Exception {
		Work work = new Work();
		work.setWorkName("Test 1");
		work.setWorkStatus(WorkStatus.PLANNING);

		when(workService.findById(any())).thenReturn(work);

		mvc.perform(get("/v1/api/works/1")).andExpect(status().isOk()).andExpect(jsonPath("workName", is("Test 1")))
				.andExpect(jsonPath("workStatus", is("PLANNING")));

		verify(workService, times(1)).findById(any());
	}

	@Test
	public void test_find_all() throws Exception {

		LocalDateTime dateTime = LocalDateTime.of(2019, 10, 03, 0, 0, 0);
		List<Work> list = Arrays.asList(new Work(1L, "Test 1", dateTime, dateTime, WorkStatus.PLANNING),
				new Work(2L, "Test 2", dateTime, dateTime, WorkStatus.COMPLETE),
				new Work(3L, "Test 3", dateTime, dateTime, WorkStatus.DOING));

		when(workService.findAll((any()))).thenReturn(new PageImpl<>(list));

		mvc.perform(get("/v1/api/works").param("sortBy", "id").param("order", "asc").param("page", "0")
				.param("pageSize", "10")).andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(3)))
				.andExpect(jsonPath("$.content[0].id", is(1)))
				.andExpect(jsonPath("$.content[0].workName", is("Test 1")))
				.andExpect(jsonPath("$.content[0].workStatus", is("PLANNING")))
				.andExpect(jsonPath("$.content[1].id", is(2)))
				.andExpect(jsonPath("$.content[1].workName", is("Test 2")))
				.andExpect(jsonPath("$.content[1].workStatus", is("COMPLETE")))
				.andExpect(jsonPath("$.content[2].id", is(3)))
				.andExpect(jsonPath("$.content[2].workName", is("Test 3")))
				.andExpect(jsonPath("$.content[2].workStatus", is("DOING")));
		verify(workService, times(1)).findAll(any());
	}

	@Test
	public void test_deleteWork_success() throws Exception {
		mvc.perform(delete("/v1/api/works/1/delete")).andExpect(status().isNoContent());
		verify(workService, times(1)).delete(any());
	}

	@Test
	public void test_add_work_success() throws Exception {
		String workName = "Test 01";
		WorkStatus workStatus = WorkStatus.PLANNING;

		WorkRequest workRequest = new WorkRequest();
		workRequest.setWorkName(workName);
		workRequest.setWorkStatus(workStatus);

		when(workService.add(any())).thenReturn(AppUltis.convertToEntity(workRequest, new Work()));
		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(workRequest);

		mvc.perform(post("/v1/api/works/add").contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJson))
				.andExpect(status().isCreated()).andExpect(jsonPath("workName", is("Test 01")))
				.andExpect(jsonPath("workStatus", is("PLANNING")));
		verify(workService, times(1)).add(any());
		verifyNoMoreInteractions(workService);
	}

	@Test
	public void test_add_work_failed() throws Exception {
		String workName = "";
		WorkStatus workStatus = WorkStatus.PLANNING;

		WorkRequest workRequest = new WorkRequest();
		workRequest.setWorkName(workName);
		workRequest.setWorkStatus(workStatus);
		
		when(workService.add(any())).thenReturn(AppUltis.convertToEntity(workRequest, new Work()));
		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(workRequest);

		mvc.perform(post("/v1/api/works/add").contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJson))
				.andExpect(status().isBadRequest());
		verify(workService, times(0)).add(any());
	}

	@Test
	public void test_edit_work_success() throws Exception {
		String workName = "Test 01";
		WorkStatus workStatus = WorkStatus.COMPLETE;

		WorkRequest workRequest = new WorkRequest();
		workRequest.setWorkName(workName);
		workRequest.setWorkStatus(workStatus);

		when(workService.edit(any(), any())).thenReturn(AppUltis.convertToEntity(workRequest, new Work()));
		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(workRequest);

		mvc.perform(put("/v1/api/works/1/edit").contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJson))
				.andExpect(status().isOk()).andExpect(jsonPath("workName", is("Test 01")))
				.andExpect(jsonPath("workStatus", is("COMPLETE")));

		verify(workService, times(1)).edit(any(), any());
		verifyNoMoreInteractions(workService);
	}

	@Test
	public void test_edit_work_failed() throws Exception {
		String workName = "";
		WorkStatus workStatus = WorkStatus.PLANNING;

		WorkRequest workRequest = new WorkRequest();
		workRequest.setWorkName(workName);
		workRequest.setWorkStatus(workStatus);
		
		when(workService.edit(any(), any())).thenReturn(AppUltis.convertToEntity(workRequest, new Work()));
		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(workRequest);

		mvc.perform(put("/v1/api/works/1/edit").contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJson))
				.andExpect(status().isBadRequest());
		verify(workService, times(0)).add(any());
	}
}
