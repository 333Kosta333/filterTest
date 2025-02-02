package org.example.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.example.entity.RequestEntity;
import org.example.service.RequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest/request")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public Page<RequestEntity> getAll(@RequestParam String filter, Pageable pageable) {
        return requestService.getAll(filter, pageable);
    }

    @GetMapping("/{id}")
    public RequestEntity getOne(@PathVariable UUID id) {
        return requestService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<RequestEntity> getMany(@RequestParam List<UUID> ids) {
        return requestService.getMany(ids);
    }

    @PostMapping
    public RequestEntity create(@RequestBody RequestEntity requestEntity) {
        return requestService.create(requestEntity);
    }

    @PatchMapping("/{id}")
    public RequestEntity patch(@PathVariable UUID id, @RequestBody JsonNode patchNode) throws IOException {
        return requestService.patch(id, patchNode);
    }

    @PatchMapping
    public List<UUID> patchMany(@RequestParam List<UUID> ids, @RequestBody JsonNode patchNode) throws IOException {
        return requestService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public RequestEntity delete(@PathVariable UUID id) {
        return requestService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<UUID> ids) {
        requestService.deleteMany(ids);
    }
}
