package com.example.backend.controller;

import com.example.backend.dto.MetaStatsDto;
import com.example.backend.service.StatsService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stats")
@AllArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/deck/{deckId}/by-meta")
    public ResponseEntity<List<MetaStatsDto>> statsByDeck(
            @PathVariable Long deckId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false, defaultValue = "true") boolean includeNoMeta
    ) {
        return ResponseEntity.ok(statsService.getStatsByDeck(deckId, fromDate, toDate, includeNoMeta));
    }
}
