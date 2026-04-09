package com.uptraceiq.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uptraceiq.dto.CreateEndpointRequest;
import com.uptraceiq.dto.EndpointDTO;
import com.uptraceiq.mapper.EndpointMapper;
import com.uptraceiq.model.Endpoint;
import com.uptraceiq.repository.EndpointRepository;
import com.uptraceiq.repository.HealthCheckResultRepository;

@RestController
@RequestMapping("/api/endpoints")
public class EndpointController {
    
    private final EndpointRepository endpointRepository;
    private final EndpointMapper endpointMapper;
    private final HealthCheckResultRepository resultRepository;

    public EndpointController(EndpointRepository endpointRepository, 
                                EndpointMapper endpointMapper,
                                HealthCheckResultRepository resultRepository) {
        this.endpointRepository = endpointRepository;
        this.endpointMapper = endpointMapper;
        this.resultRepository = resultRepository;
    }

    @GetMapping
    public List<EndpointDTO> getAllEndpoints() {
        List<Endpoint> endpoints = endpointRepository.findAll();
        return endpoints.stream()
            .map(endpointMapper::toDTO)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EndpointDTO> getEndpointById(@PathVariable Long id) {
        return endpointRepository.findById(id)
            .map(entity -> ResponseEntity.ok(endpointMapper.toDTO(entity)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EndpointDTO> createEndpoint(@RequestBody CreateEndpointRequest request) {
        Endpoint entity = endpointMapper.toEntity(request);
        Endpoint saved = endpointRepository.save(entity);
        EndpointDTO dto = endpointMapper.toDTO(saved);
        return ResponseEntity.status(201).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndpoint(@PathVariable Long id) {
        if (!endpointRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        resultRepository.deleteByEndpointId(id);
        endpointRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
