package com.taskagent.repository;

import com.taskagent.domain.Agent;
import com.taskagent.domain.Skill;
import com.taskagent.domain.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@DirtiesContext
@RunWith(SpringRunner.class)
public class RepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    AgentRepository agentRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SkillRepository skillRepository;

    @Test
    public void testGetAgentsBySkills() {
        Skill skill1 = new Skill();
        skill1.setName("skill4");

        Skill skill2 = new Skill();
        skill2.setName("skill5");

        Skill skill3 = new Skill();
        skill3.setName("skill6");

        entityManager.persist(skill1);
        entityManager.persist(skill2);
        entityManager.persist(skill3);

        Agent agent1 = new Agent();
        agent1.setSkills(Arrays.asList(skill1, skill2, skill3));

        Agent agent2 = new Agent();
        agent2.setSkills(Arrays.asList(skill1));

        Agent agent3 = new Agent();
        agent3.setSkills(Arrays.asList(skill2, skill3));

        entityManager.persist(agent1);
        entityManager.persist(agent2);
        entityManager.persist(agent3);
        entityManager.flush();

        List<Agent> agents = agentRepository.findAgentsBySkillsAndNotInUse(Arrays.asList(skill1.getId()), 1)
                .orElseThrow(RuntimeException::new);

        assertEquals(2, agents.size());
    }

    @Test
    public void testGetAgentsBySkillsAndNotInUse() {
        Skill skill1 = new Skill();
        skill1.setName("skill4");

        Skill skill2 = new Skill();
        skill2.setName("skill5");

        Skill skill3 = new Skill();
        skill3.setName("skill6");
        entityManager.persist(skill1);
        entityManager.persist(skill2);
        entityManager.persist(skill3);

        Agent agent1 = new Agent();
        agent1.setSkills(Arrays.asList(skill1, skill2, skill3));

        Agent agent2 = new Agent();
        agent2.setSkills(Arrays.asList(skill1));

        Agent agent3 = new Agent();
        agent3.setSkills(Arrays.asList(skill2, skill3));

        entityManager.persist(agent1);
        entityManager.persist(agent2);
        entityManager.persist(agent3);

        Task task = new Task();
        task.setPriority("low");
        task.setAgent(agent2);
        agent2.setTask(task);

        entityManager.persist(task);
        entityManager.flush();

        List<Agent> agents = agentRepository.findAgentsBySkillsAndNotInUse(Arrays.asList(skill1.getId()), 1)
                .orElseThrow(RuntimeException::new);

        assertEquals(1, agents.size());
    }

    @Test
    public void testFindAgentsBySkillsAndLowPriorityOrderByRecent() throws InterruptedException {
        Skill skill1 = new Skill();
        skill1.setName("skill4");

        Skill skill2 = new Skill();
        skill2.setName("skill5");

        Skill skill3 = new Skill();
        skill3.setName("skill6");
        entityManager.persist(skill1);
        entityManager.persist(skill2);
        entityManager.persist(skill3);

        Agent agent1 = new Agent();
        agent1.setSkills(Arrays.asList(skill1, skill2, skill3));

        Agent agent2 = new Agent();
        agent2.setSkills(Arrays.asList(skill1));

        Agent agent3 = new Agent();
        agent3.setSkills(Arrays.asList(skill2, skill3));

        entityManager.persist(agent1);
        entityManager.persist(agent2);
        entityManager.persist(agent3);

        Task task1 = new Task();
        task1.setPriority("low");
        task1.setAgent(agent1);
        agent1.setTask(task1);
        entityManager.persist(task1);

        Thread.sleep(100L);

        Task task2 = new Task();
        task2.setPriority("low");
        task2.setAgent(agent2);
        agent2.setTask(task2);
        entityManager.persist(task2);
        entityManager.flush();

        List<Agent> agents = agentRepository.findAgentsBySkillsAndLowPriorityOrderByRecent(Arrays.asList(skill1.getId()), 1)
                .orElseThrow(RuntimeException::new);

        assertEquals(2, agents.size());
        Agent mostRecent = agents.get(0);
        Agent leastRecent = agents.get(1);
        assertTrue(mostRecent.getTask().getCreatedAt().isAfter(leastRecent.getTask().getCreatedAt()));
    }


}
