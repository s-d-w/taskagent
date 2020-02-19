package com.taskagent.dto;

import com.taskagent.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id;

    private String priority;

    private Instant createdAt;

    public static TaskDTO fromTask(Task task) {
        return new TaskDTO(task.getId(), task.getPriority(), task.getCreatedAt());
    }

}
