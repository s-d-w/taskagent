package com.taskagent.repository;

import com.taskagent.domain.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    @Query(value = "SELECT a.* FROM agent a " +
                        "INNER JOIN agent_skill ags ON ags.agent_id = a.id " +
                        "INNER JOIN skill s ON s.id = ags.skill_id " +
                        "WHERE s.id IN (:skills) AND a.id NOT IN (SELECT t.agent_id FROM task t) " +
                        "GROUP BY a.id " +
                        "HAVING COUNT(*) = :size", nativeQuery = true)
    Optional<List<Agent>> findAgentsBySkillsAndNotInUse(@Param("skills") List<Long> skills, @Param("size") int size);

    @Query(value = "SELECT a.* FROM agent a " +
            "INNER JOIN agent_skill ags ON ags.agent_id = a.id " +
            "INNER JOIN skill s ON s.id = ags.skill_id " +
            "INNER JOIN task t ON t.agent_id = a.id " +
            "WHERE s.id IN (:skills) AND t.priority = 'low' " +
            "GROUP BY a.id, t.created_at " +
            "HAVING COUNT(*) = :size " +
            "ORDER BY t.created_at DESC", nativeQuery = true)
    Optional<List<Agent>> findAgentsBySkillsAndLowPriorityOrderByRecent(@Param("skills") List<Long> skills, @Param("size") int size);

}
