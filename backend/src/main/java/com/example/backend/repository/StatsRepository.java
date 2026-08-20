package com.example.backend.repository;

import com.example.backend.dto.MetaStatsDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatsRepository {

    private final JdbcTemplate jdbc;

    public StatsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<MetaStatsDto> findMetaStatsByDeck(Long deckId, LocalDate fromDate, LocalDate toDate, boolean includeNoMeta) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
           .append("COALESCE(m.id, 0) AS meta_id, ")
           .append("COALESCE(m.name, 'UNASSIGNED') AS meta_name, ")
           .append("COUNT(DISTINCT mt.id) AS matches_played, ")
           .append("COUNT(g.id) AS games_played, ")
           .append("SUM(CASE WHEN g.result = 'WIN' THEN 1 ELSE 0 END) AS wins, ")
           .append("SUM(CASE WHEN g.result = 'LOSS' THEN 1 ELSE 0 END) AS losses, ")
           .append("SUM(CASE WHEN g.result = 'DRAW' THEN 1 ELSE 0 END) AS draws, ")
           .append("CASE WHEN COUNT(g.id)=0 THEN 0.0 ELSE 100.0 * SUM(CASE WHEN g.result = 'WIN' THEN 1 ELSE 0 END) / COUNT(g.id) END AS game_win_rate ")
           .append("FROM matches mt \n")
           .append("LEFT JOIN opponent_decks od ON mt.opponent_deck_id = od.id \n")
           .append("LEFT JOIN metas m ON od.meta_id = m.id \n")
           .append("LEFT JOIN games g ON g.match_id = mt.id \n")
           .append("WHERE mt.deck_id = ? ");

        List<Object> params = new ArrayList<>();
        params.add(deckId);

        if (fromDate != null) {
            sql.append(" AND mt.match_date >= ? ");
            params.add(Date.valueOf(fromDate));
        }
        if (toDate != null) {
            sql.append(" AND mt.match_date <= ? ");
            params.add(Date.valueOf(toDate));
        }
        if (!includeNoMeta) {
            sql.append(" AND m.id IS NOT NULL ");
        }

        sql.append(" GROUP BY m.id, m.name \n")
           .append("ORDER BY matches_played DESC");

        RowMapper<MetaStatsDto> mapper = (rs, rowNum) -> new MetaStatsDto(
                rs.getLong("meta_id") == 0 ? null : rs.getLong("meta_id"),
                rs.getString("meta_name"),
                rs.getLong("matches_played"),
                rs.getLong("games_played"),
                rs.getLong("wins"),
                rs.getLong("losses"),
                rs.getLong("draws"),
                rs.getDouble("game_win_rate")
        );

        return jdbc.query(sql.toString(), params.toArray(), mapper);
    }
}
