package com.example.backend.controller;

import com.example.backend.dto.MetaInputDto;
import com.example.backend.entity.Meta;
import com.example.backend.service.MetaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meta")
@AllArgsConstructor
public class MetaController {

    private final MetaService metaService;

    @PostMapping("/create")
    public ResponseEntity<Meta> create(@Valid @RequestBody MetaInputDto input) {
        return ResponseEntity.ok(metaService.create(input));
    }
}
