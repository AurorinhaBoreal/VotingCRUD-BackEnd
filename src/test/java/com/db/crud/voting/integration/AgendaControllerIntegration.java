package com.db.crud.voting.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.db.crud.voting.VotingApplication;
import com.db.crud.voting.fixture.SqlProvider;

@AutoConfigureMockMvc
@SpringBootTest(classes = {VotingApplication.class})
@ActiveProfiles("test")
public class AgendaControllerIntegration {
    
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Happy Test: Should get Active Agendas")
    @SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.INSERT_AGENDA),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.CLEAR_DB)
    })
    void getActiveAgendas() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agenda/active"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].question").value("Do you like Air-Fryers?"));
    }

    @Test
    @DisplayName("Happy Test: Should get Ended Agendas")
    @SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.INSERT_AGENDA),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.CLEAR_DB)
    })
    void getEndedAgendas() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agenda"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].question").value("Do you like leap years?"));
    }
}
