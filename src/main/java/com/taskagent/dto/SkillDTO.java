package com.taskagent.dto;

import com.taskagent.domain.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillDTO {

    private Long id;

    private String name;

    public static SkillDTO fromSkill(Skill skill) {
        return new SkillDTO(skill.getId(), skill.getName());
    }
}
