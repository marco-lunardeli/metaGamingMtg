package com.example.backend.service.impl;

import com.example.backend.dto.OpponentDeckInputDto;
import com.example.backend.entity.Meta;
import com.example.backend.entity.OpponentDeck;
import com.example.backend.repository.MetaRepository;
import com.example.backend.repository.OpponentDeckRepository;
import com.example.backend.service.OpponentDeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OpponentDeckServiceImpl implements OpponentDeckService {

    private final OpponentDeckRepository opponentDeckRepository;
    private final MetaRepository metaRepository;

    @Override
    @Transactional
    public OpponentDeck create(OpponentDeckInputDto input) {
        Meta meta = null;
        if (input.metaId() != null) {
            meta = metaRepository.findById(input.metaId())
                    .orElseThrow(() -> new IllegalArgumentException("Meta not found"));
        }

        OpponentDeck deck = OpponentDeck.builder()
                .name(input.name())
                .meta(meta)
                .build();

        return opponentDeckRepository.save(deck);
    }
}
