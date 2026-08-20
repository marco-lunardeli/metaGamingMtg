package com.example.backend.service.impl;

import com.example.backend.dto.MetaStatsDto;
import com.example.backend.repository.StatsRepository;
import com.example.backend.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public List<MetaStatsDto> getStatsByDeck(Long deckId, LocalDate fromDate, LocalDate toDate, boolean includeNoMeta) {
        return statsRepository.findMetaStatsByDeck(deckId, fromDate, toDate, includeNoMeta);
    }
}
