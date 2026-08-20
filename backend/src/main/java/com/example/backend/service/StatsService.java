package com.example.backend.service;

import com.example.backend.dto.MetaStatsDto;

import java.time.LocalDate;
import java.util.List;

public interface StatsService {
    List<MetaStatsDto> getStatsByDeck(Long deckId, LocalDate fromDate, LocalDate toDate, boolean includeNoMeta);
}
