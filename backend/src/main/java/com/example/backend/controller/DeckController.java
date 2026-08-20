package com.example.backend.controller;

import com.example.backend.dto.DeckInputDto;
import com.example.backend.entity.Deck;
import com.example.backend.service.DeckService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deck")
@AllArgsConstructor
public class DeckController {

    private final DeckService deckService;

    @PostMapping("/create")
    public ResponseEntity<Deck> create(@Valid @RequestBody DeckInputDto input) {
        return ResponseEntity.ok(deckService.create(input));
    }
}
