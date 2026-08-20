package com.example.backend.service;

import com.example.backend.dto.OpponentDeckInputDto;
import com.example.backend.entity.OpponentDeck;

public interface OpponentDeckService {
    OpponentDeck create(OpponentDeckInputDto input);
}
