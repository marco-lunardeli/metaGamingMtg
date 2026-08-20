package com.example.backend.controller;

import com.example.backend.dto.GameInputDto;
import com.example.backend.entity.Game;
import com.example.backend.service.GameService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<Game> create(@Valid @RequestBody GameInputDto input) {
        return ResponseEntity.ok(gameService.create(input));
    }
}
