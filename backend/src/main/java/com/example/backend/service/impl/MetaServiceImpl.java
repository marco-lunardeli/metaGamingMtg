package com.example.backend.service.impl;

import com.example.backend.dto.MetaInputDto;
import com.example.backend.entity.Meta;
import com.example.backend.entity.User;
import com.example.backend.repository.MetaRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.MetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MetaServiceImpl implements MetaService {

    private final MetaRepository metaRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Meta create(MetaInputDto input) {
        User user = null;
        if (input.userId() != null) {
            user = userRepository.findById(input.userId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        }

        Meta meta = Meta.builder()
                .name(input.name())
                .user(user)
                .build();

        return metaRepository.save(meta);
    }
}
