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
public class LogControllerIntegration {
    
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Happy Test: Should get Specific")
    @SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.CLEAR_DB),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.INSERT_LOG),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.CLEAR_DB)
    })
    void getLogs() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/log"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].objectType").value("Agenda"))
            .andExpect(jsonPath("$[0].objectId").value(1))
            .andExpect(jsonPath("$[0].objectInfo").value("Do you like Tennis?"))
            .andExpect(jsonPath("$[0].operation").value("CREATE"))
            .andExpect(jsonPath("$[0].realizedOn").value("2024-08-12T11:00:00")); 
    }
}