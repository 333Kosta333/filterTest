package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.entity.RequestEntity;
import org.example.repository.RequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final ObjectMapper objectMapper;

    @Override
    public Page<RequestEntity> getAll(String filter, Pageable pageable) {

//        Specification<RequestEntity> spec = filter.toSpecification();
//        return requestRepository.findAll(spec, pageable);
        return requestRepository.findAll(pageable);
    }

    @Override
    public RequestEntity getOne(UUID id) {
        Optional<RequestEntity> requestEntityOptional = requestRepository.findById(id);
        return requestEntityOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @Override
    public List<RequestEntity> getMany(List<UUID> ids) {
        return requestRepository.findAllById(ids);
    }

    @Override
    public RequestEntity create(RequestEntity requestEntity) {
        return requestRepository.save(requestEntity);
    }

    @Override
    public RequestEntity patch(UUID id, JsonNode patchNode) throws IOException {
        RequestEntity requestEntity = requestRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(requestEntity).readValue(patchNode);

        return requestRepository.save(requestEntity);
    }

    @Override
    public List<UUID> patchMany(List<UUID> ids, JsonNode patchNode) throws IOException {
        Collection<RequestEntity> requestEntities = requestRepository.findAllById(ids);

        for (RequestEntity requestEntity : requestEntities) {
            objectMapper.readerForUpdating(requestEntity).readValue(patchNode);
        }

        List<RequestEntity> resultRequestEntities = requestRepository.saveAll(requestEntities);
        return resultRequestEntities.stream()
                .map(RequestEntity::getId)
                .toList();
    }

    @Override
    public RequestEntity delete(UUID id) {
        RequestEntity requestEntity = requestRepository.findById(id).orElse(null);
        if (requestEntity != null) {
            requestRepository.delete(requestEntity);
        }
        return requestEntity;
    }

    @Override
    public void deleteMany(List<UUID> ids) {
        requestRepository.deleteAllById(ids);
    }
}
