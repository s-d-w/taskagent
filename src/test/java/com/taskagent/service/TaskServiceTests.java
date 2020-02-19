package com.taskagent.service;

import com.taskagent.domain.Agent;
import com.taskagent.domain.Skill;
import com.taskagent.domain.Task;
import com.taskagent.dto.request.TaskCreateRequest;
import com.taskagent.repository.AgentRepository;
import com.taskagent.repository.SkillRepository;
import com.taskagent.repository.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
public class TaskServiceTests {

    @MockBean
    AgentRepository agentRepository;

    @MockBean
    TaskRepository taskRepository;

    @MockBean
    SkillRepository skillRepository;

    @Captor
    private ArgumentCaptor<Task> taskCaptor;

    TaskService taskService;

    @Before
    public void setUp() {
        Skill skill = new Skill();
        skill.setId(1L);
        skill.setName("skill1");
        Mockito.when(skillRepository.findByNameIn(any()))
                .thenReturn(Collections.singletonList(skill));

        Agent agent = new Agent();
        agent.setSkills(Collections.singletonList(skill));
        agent.setId(2L);
        Mockito.when(agentRepository.findAgentsBySkillsAndNotInUse(any(), eq(1)))
                .thenReturn(Optional.of(Collections.singletonList(agent)));

        taskService = new TaskService(taskRepository, agentRepository, skillRepository);
    }

    @Test
    public void testCreateAndAssignTaskSimpleCase() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setSkills(Collections.singletonList("skill1"));
        request.setPriority("low");

        taskService.createAndAssignTask(request);

        Mockito.verify(taskRepository).save(taskCaptor.capture());

        Task capturedTask = taskCaptor.getValue();

        assertThat(capturedTask.getPriority())
                .isEqualTo("low");

        assertThat(capturedTask.getAgent()).isNotNull();

        assertThat(capturedTask.getAgent().getId())
                .isEqualTo(2L);
    }

}
