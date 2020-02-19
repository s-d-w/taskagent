package com.taskagent.service;

import com.taskagent.domain.Agent;
import com.taskagent.domain.Skill;
import com.taskagent.domain.Task;
import com.taskagent.dto.request.TaskCreateRequest;
import com.taskagent.exception.BadRequestException;
import com.taskagent.exception.NotFoundException;
import com.taskagent.exception.TaskSchedulingException;
import com.taskagent.repository.AgentRepository;
import com.taskagent.repository.SkillRepository;
import com.taskagent.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService {

    private TaskRepository taskRepository;

    private AgentRepository agentRepository;

    private SkillRepository skillRepository;

    public TaskService(TaskRepository taskRepository,
                       AgentRepository agentRepository,
                       SkillRepository skillRepository) {
        this.taskRepository = taskRepository;
        this.agentRepository = agentRepository;
        this.skillRepository = skillRepository;
    }

    @Transactional
    public Task createAndAssignTask(TaskCreateRequest request) {
        List<String> skillsStrings = request.getSkills();
        List<Skill> skills = skillRepository.findByNameIn(skillsStrings);
        if (skills.size() != skillsStrings.size()) {
            throw new BadRequestException("One or more unknown skills present in task creation request");
        }
        List<Long> skillIds = skills.stream().map(Skill::getId).collect(Collectors.toList());
        Optional<List<Agent>> agentsOptional = agentRepository.findAgentsBySkillsAndNotInUse(skillIds, skillIds.size());
        if (!agentsOptional.isPresent() && "high".equals(request.getPriority())) {
            agentsOptional = agentRepository.findAgentsBySkillsAndLowPriorityOrderByRecent(skillIds, skillIds.size());
            if (!agentsOptional.isPresent()) {
                throw new TaskSchedulingException("No agents available to handle high priority task.");
            }
        }

        List<Agent> agents = agentsOptional
                .orElseThrow(() -> new TaskSchedulingException("No agents available to handle low priority task."));

        if ("low".equals(request.getPriority())) {
            /* use the agent with the least amount of skills that satisfies the task's skills */
            agents.sort(Comparator.comparingInt((Agent a) -> a.getSkills().size()));
        }

        Task task = new Task();
        task.setPriority(request.getPriority());
        Agent agent = agents.get(0);
        if (agent.getTask() != null) {
            log.info("Agent already assigned a task. Deleting previous task: " + agent.getTask().getId());
            taskRepository.delete(agent.getTask());
            taskRepository.flush();
        }
        agent.setTask(task);
        task.setAgent(agent);
        log.info("Assigning agent with agent_id: " + agent.getId() + " to new task.");
        return taskRepository.save(task);
    }

    @Transactional
    public void completeTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
        log.info("Deleting task: " + task.getId());
        taskRepository.delete(task);
    }
}
