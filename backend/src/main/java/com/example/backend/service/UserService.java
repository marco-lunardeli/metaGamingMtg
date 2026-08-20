package com.example.backend.service;

import com.example.backend.dto.UserInputDto;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(UserInputDto input) {

        validateUserName(input.username());

        User user = User.builder()
                .username(input.username())
                .creationDate(OffsetDateTime.now())
                .updateDate(OffsetDateTime.now())
                .build();

        return userRepository.save(user);
    }

    private void validateUserName(String username) {
        var userNameDb = this.getUserByUsername(username);

        if(userNameDb != null) {
            //TODO ADD A ESPECIF EXCEPTION AND INCLUDE A ADVISER TO PROPAGATE ITS HTTP STATUS
            throw new RuntimeException("Username already exists");
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
