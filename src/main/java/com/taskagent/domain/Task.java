package com.taskagent.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String priority;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @OneToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

}
