package com.example.backend.service;

import com.example.backend.dto.GameInputDto;
import com.example.backend.entity.Game;

public interface GameService {
    Game create(GameInputDto input);
}
