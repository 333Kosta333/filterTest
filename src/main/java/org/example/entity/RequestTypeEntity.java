package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "request_types", schema = "public")
public class RequestTypeEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, unique = true, length = 2)
    private String id;

    @Column(name = "name", length = 60)
    private String name;

    @Column(name = "description")
    private String description;

}
