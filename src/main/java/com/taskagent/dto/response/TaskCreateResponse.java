package com.taskagent.dto.response;

import com.taskagent.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateResponse {

    private Long taskId;

    private Long agentId;

    private String priority;

    public static TaskCreateResponse fromTask(Task task) {
        return new TaskCreateResponse(task.getId(), task.getAgent().getId(), task.getPriority());
    }
}
