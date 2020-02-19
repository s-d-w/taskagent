package com.taskagent.controller;

import com.taskagent.dto.AgentDTO;
import com.taskagent.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class AgentController {

    private AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping(path = "/agent", produces = {"application/json"})
    public ResponseEntity<List<AgentDTO>> getAgents() {
        List<AgentDTO> agentDTOs = agentService.getAllAgents().stream()
                .map(AgentDTO::fromAgent).collect(Collectors.toList());
        return new ResponseEntity<>(agentDTOs, HttpStatus.OK);
    }
}
