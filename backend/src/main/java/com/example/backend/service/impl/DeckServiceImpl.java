package com.example.backend.service.impl;

import com.example.backend.dto.DeckInputDto;
import com.example.backend.entity.Deck;
import com.example.backend.entity.Format;
import com.example.backend.entity.User;
import com.example.backend.repository.DeckRepository;
import com.example.backend.repository.FormatRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.DeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {

    private final DeckRepository deckRepository;
    private final UserRepository userRepository;
    private final FormatRepository formatRepository;

    @Override
    @Transactional
    public Deck create(DeckInputDto input) {
        User user = userRepository.findById(input.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Format format = null;
        if (input.formatId() != null) {
            format = formatRepository.findById(input.formatId())
                    .orElseThrow(() -> new IllegalArgumentException("Format not found"));
        }

        Deck deck = Deck.builder()
                .name(input.name())
                .user(user)
                .format(format)
                .version(input.version() != null ? input.version() : 1)
                .build();

        return deckRepository.save(deck);
    }
}
