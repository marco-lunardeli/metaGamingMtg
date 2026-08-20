package com.example.backend.service;

import com.example.backend.dto.MatchInputDto;
import com.example.backend.entity.Match;

public interface MatchService {
    Match create(MatchInputDto input);
}
