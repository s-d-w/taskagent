### Taskagent

Taskagent is an application that schedules tasks to agents. A task has a specific skill 
set that agents need to have in order to be scheduled the task. Tasks also have a low and 
high priority. A high priority task is immediately scheduled to the most recent scheduled 
agent that is working on a low priority task. The previous low priority task is deleted.

#### Requirements
- Docker

### Building and Running

Build and run with docker: `docker-compose up --build`

Docker is configured to test, compile, and build the application without any other installed 
dependencies other than docker. However, you can use the included gradle wrapper to run it 
without docker.

Run just the tests: `./gradlew test`

Run it stand alone without docker-compose: `./gradew bootRun`

When the app runs stand alone via gradle without docker-compose, it will use embedded H2.

### REST Endpoints

The application has three REST endpoints.

 `POST /task - see example payload below`
 
 `GET /agent - retrieves a list of all agents and their assigned tasks if any`
 
 `PUT /task/{id}/complete - completes a task, which deletes the task and unassociates it from the agent`

Create some tasks: 

`curl -XPOST -v -H "Content-Type: application/json" -d "{\"priority\": \"low\", \"skills\": [\"skill1\"]}" localhost:8080/task | jq`

`curl -XPOST -v -H "Content-Type: application/json" -d "{\"priority\": \"low\", \"skills\": [\"skill1\", \"skill2\"]}" localhost:8080/task | jq`

`curl -XPOST -v -H "Content-Type: application/json" -d "{\"priority\": \"high\", \"skills\": [\"skill1\", \"skill2\", \"skill3\"]}" localhost:8080/task | jq`

Complete a task (change 1 to the id of the task - you can see the task id in the response when creating the task):

`curl -XPUT -v localhost:8080/task/1/complete | jq`

Get a list of all agents and their associated skills and task:

`curl -XGET -v localhost:8080/agent | jq`

### Misc

You can access the running H2 (stand alone without docker) by going here: `http://localhost:8080/h2/`

When connecting to H2, use the defaults, but use this as the JDBC url: `jdbc:h2:mem:testdb`

When using docker-compose, you can access postgres via: `psql -h localhost -d taskagentdb -U taskagent-user`

The password is `abc123`

### SQL for validation
~~~~

select * from task t;

select * from agent a
    INNER JOIN agent_skill ags ON ags.agent_id = a.id
    INNER JOIN skill s ON s.id = ags.skill_id;
~~~~

Find agents that are not in use that satisfy the (two) skills with ids 1 and 2:
~~~~
select * from skill s;

SELECT a.* FROM agent a
    INNER JOIN agent_skill ags ON ags.agent_id = a.id
    INNER JOIN skill s ON s.id = ags.skill_id
    WHERE s.id IN (1, 2) AND a.id NOT IN (SELECT t.agent_id FROM task t)
    GROUP BY a.id
    HAVING COUNT(*) = 2;
~~~~

Find agents that are in use with low priority tasks that satisfy the skill with id 1:
~~~~
SELECT a.* FROM agent a
    INNER JOIN agent_skill ags ON ags.agent_id = a.id
    INNER JOIN skill s ON s.id = ags.skill_id
    INNER JOIN task t ON t.agent_id = a.id
    WHERE s.id IN (1) AND t.priority = 'low'
    GROUP BY a.id, t.created_at
    HAVING COUNT(*) = 1
    ORDER BY t.created_at DESC;
~~~~
