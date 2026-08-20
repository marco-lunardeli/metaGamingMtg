package com.example.backend.controller;

import com.example.backend.dto.MatchInputDto;
import com.example.backend.entity.Match;
import com.example.backend.service.MatchService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/match")
@AllArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/create")
    public ResponseEntity<Match> create(@Valid @RequestBody MatchInputDto input) {
        return ResponseEntity.ok(matchService.create(input));
    }
}
