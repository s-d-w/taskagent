package com.taskagent.dto;

import com.taskagent.domain.Agent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentDTO {

    private Long id;

    private List<SkillDTO> skills;

    private TaskDTO task;

    public static AgentDTO fromAgent(Agent agent) {
        List<SkillDTO> skillDTOs = agent.getSkills().stream().map(SkillDTO::fromSkill).collect(Collectors.toList());
        TaskDTO taskDTO = null;
        if (agent.getTask() != null) {
            taskDTO = TaskDTO.fromTask(agent.getTask());
        }
        return new AgentDTO(agent.getId(), skillDTOs, taskDTO);
    }
}
