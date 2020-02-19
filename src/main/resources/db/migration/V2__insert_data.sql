INSERT INTO agent (id) VALUES (1);
INSERT INTO agent (id) VALUES (2);
INSERT INTO agent (id) VALUES (3);

INSERT INTO skill (name) VALUES ('skill1');
INSERT INTO skill (name) VALUES ('skill2');
INSERT INTO skill (name) VALUES ('skill3');

INSERT INTO agent_skill (agent_id, skill_id) VALUES (1, (SELECT id FROM skill WHERE name = 'skill1'));
INSERT INTO agent_skill (agent_id, skill_id) VALUES (1, (SELECT id FROM skill WHERE name = 'skill2'));
INSERT INTO agent_skill (agent_id, skill_id) VALUES (1, (SELECT id FROM skill WHERE name = 'skill3'));

INSERT INTO agent_skill (agent_id, skill_id) VALUES (2, (SELECT id FROM skill WHERE name = 'skill1'));

INSERT INTO agent_skill (agent_id, skill_id) VALUES (3, (SELECT id FROM skill WHERE name = 'skill2'));
INSERT INTO agent_skill (agent_id, skill_id) VALUES (3, (SELECT id FROM skill WHERE name = 'skill3'));

