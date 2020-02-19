CREATE TABLE IF NOT EXISTS agent (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS skill (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR NOT NULL,
    UNIQUE KEY UK_skill_name (name)
);

CREATE INDEX indx_skill_name ON skill(name);

CREATE TABLE IF NOT EXISTS agent_skill (
    agent_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    CONSTRAINT fk_agent_agent_skill FOREIGN KEY (agent_id) REFERENCES agent(id),
    CONSTRAINT fk_skill_agent_skill FOREIGN KEY (skill_id) REFERENCES skill(id)
);

CREATE INDEX indx_agent_id_agent_skill ON agent_skill(agent_id);
CREATE INDEX indx_skill_id_agent_skill ON agent_skill(skill_id);

CREATE TABLE IF NOT EXISTS task (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    priority VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    agent_id BIGINT NOT NULL,
    UNIQUE KEY UK_task_agent_id (agent_id)
);

CREATE INDEX indx_task_agent_id ON task(agent_id);
