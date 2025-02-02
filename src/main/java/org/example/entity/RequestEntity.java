package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RequestEntity implements Serializable {
    @ToString.Include
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "template_id")
    private String templateId;

    @ToString.Include
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private RequestTypeEntity requestType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "struct_type_id")
    private RequestTypeEntity requestStructType;

    @ToString.Include
    private LocalDateTime createOn = LocalDateTime.now();

    @OneToMany(mappedBy = "requestEntity", orphanRemoval = true)
    private Set<PartnerEntity> partnerEntities = new HashSet<>();

    @OneToMany(mappedBy = "requestEntity", orphanRemoval = true)
    private Set<ServiceObjectEntity> serviceObjectEntities = new HashSet<>();

}
