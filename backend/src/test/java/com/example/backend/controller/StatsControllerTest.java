package com.example.backend.controller;

import com.example.backend.dto.MetaStatsDto;
import com.example.backend.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsController.class)
public class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService statsService;

    @Test
    public void testStatsByDeckReturnsOk() throws Exception {
        MetaStatsDto dto = new MetaStatsDto(1L, "Aggro", 2L, 6L, 4L, 1L, 1L, 66.666);
        when(statsService.getStatsByDeck(any(), any(), any(), any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/stats/deck/1/by-meta")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"metaId\":1,\"metaName\":\"Aggro\"}]", false));
    }
}
