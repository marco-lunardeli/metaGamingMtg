package com.example.backend.service;

import com.example.backend.dto.MetaInputDto;
import com.example.backend.entity.Meta;

public interface MetaService {
    Meta create(MetaInputDto input);
}
