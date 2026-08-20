package com.example.backend.repository;

import com.example.backend.entity.OpponentDeck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpponentDeckRepository extends JpaRepository<OpponentDeck, Long> {
}
