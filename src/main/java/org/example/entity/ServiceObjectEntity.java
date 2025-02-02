package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "service_objects", schema = "public")
public class ServiceObjectEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", length = 60)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "begin_work")
    private LocalDateTime beginWork;

    @Column(name = "end_work")
    private LocalDateTime endWork;

    @Column(name = "planned_date")
    private LocalDateTime plannedDate;

    @Column(name = "dead_line")
    private LocalDateTime deadLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_uuid")
    private RequestEntity requestEntity;

}