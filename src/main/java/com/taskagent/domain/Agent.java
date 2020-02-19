package com.taskagent.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "agent_skill",
            joinColumns = { @JoinColumn(name = "agent_id") },
            inverseJoinColumns = { @JoinColumn(name = "skill_id") })
    private List<Skill> skills;

    @OneToOne(mappedBy = "agent", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Task task;

}
