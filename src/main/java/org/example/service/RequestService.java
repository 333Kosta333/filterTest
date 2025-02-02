package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.entity.RequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface RequestService {
    Page<RequestEntity> getAll(String filter, Pageable pageable);

    RequestEntity getOne(UUID id);

    List<RequestEntity> getMany(List<UUID> ids);

    RequestEntity create(RequestEntity requestEntity);

    RequestEntity patch(UUID id, JsonNode patchNode) throws IOException;

    List<UUID> patchMany(List<UUID> ids, JsonNode patchNode) throws IOException;

    RequestEntity delete(UUID id);

    void deleteMany(List<UUID> ids);
}
