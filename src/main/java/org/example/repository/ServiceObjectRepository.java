package org.example.repository;

import org.example.entity.ServiceObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceObjectRepository extends JpaRepository<ServiceObjectEntity, UUID> {
}