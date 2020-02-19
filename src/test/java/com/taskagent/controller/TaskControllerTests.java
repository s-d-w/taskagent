package com.taskagent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskagent.domain.Agent;
import com.taskagent.domain.Task;
import com.taskagent.dto.request.TaskCreateRequest;
import com.taskagent.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskService taskService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreateTask() throws Exception {

        TaskCreateRequest taskCreateRequest = new TaskCreateRequest();
        taskCreateRequest.setPriority("low");
        taskCreateRequest.setSkills(Collections.singletonList("skill1"));

        Agent agent = new Agent();
        agent.setId((long) 2);

        Task task = new Task();
        task.setId((long) 1);
        task.setPriority("low");
        task.setAgent(agent);

        given(taskService.createAndAssignTask(taskCreateRequest))
                .willReturn(task);

        String body = objectMapper.writeValueAsString(taskCreateRequest);

        System.out.println(body);

        mvc.perform(post("/task")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.priority", is(task.getPriority())))
                .andExpect(jsonPath("$.taskId", is(task.getId().intValue())))
                .andExpect(jsonPath("$.agentId", is(agent.getId().intValue())));
    }

}
