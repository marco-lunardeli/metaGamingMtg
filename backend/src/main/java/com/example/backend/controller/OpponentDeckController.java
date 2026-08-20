package com.example.backend.controller;

import com.example.backend.dto.OpponentDeckInputDto;
import com.example.backend.entity.OpponentDeck;
import com.example.backend.service.OpponentDeckService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/opponent-deck")
@AllArgsConstructor
public class OpponentDeckController {

    private final OpponentDeckService opponentDeckService;

    @PostMapping("/create")
    public ResponseEntity<OpponentDeck> create(@Valid @RequestBody OpponentDeckInputDto input) {
        return ResponseEntity.ok(opponentDeckService.create(input));
    }
}
