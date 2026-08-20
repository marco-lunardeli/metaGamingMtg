package com.example.backend.service;

import com.example.backend.dto.DeckInputDto;
import com.example.backend.entity.Deck;

public interface DeckService {
    Deck create(DeckInputDto input);
}
