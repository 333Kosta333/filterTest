package org.example.repository;

import org.example.entity.RequestTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RequestTypeRepository extends JpaRepository<RequestTypeEntity, UUID> {
}