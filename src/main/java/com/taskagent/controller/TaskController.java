package com.taskagent.controller;

import com.taskagent.dto.request.TaskCreateRequest;
import com.taskagent.dto.response.TaskCreateResponse;
import com.taskagent.exception.BadRequestException;
import com.taskagent.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(path = "/task", produces = {"application/json"})
    public ResponseEntity<TaskCreateResponse> createTask(@RequestBody TaskCreateRequest request) {
        if (request.getPriority() == null || request.getPriority().isEmpty()) {
            throw new BadRequestException("Missing priority");
        } else if (!"low".equals(request.getPriority()) && !"high".equals(request.getPriority())) {
            throw new BadRequestException("Unknown priority specified");
        }
        if (request.getSkills() == null || request.getSkills().isEmpty()) {
            throw new BadRequestException("Need to specify at least one skill");
        }
        TaskCreateResponse result = TaskCreateResponse.fromTask(taskService.createAndAssignTask(request));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(path = "/task/{id}/complete", produces = {"application/json"})
    public ResponseEntity markTaskCompleted(@PathVariable Long id) {
        taskService.completeTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
