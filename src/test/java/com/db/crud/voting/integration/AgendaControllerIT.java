package com.db.crud.voting.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.db.crud.voting.VotingApplication;
import com.db.crud.voting.dto.request.VoteRequest;
import com.db.crud.voting.dto.request.AgendaRequest;
import com.db.crud.voting.fixture.AgendaFixture;
import com.db.crud.voting.fixture.SqlProvider;
import com.db.crud.voting.fixture.VoteFixture;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(classes = {VotingApplication.class})
@ActiveProfiles("test")
class AgendaControllerIT {
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private AgendaRequest agendaDTORequest = AgendaFixture.AgendaDTOValid();
    private VoteRequest voteDTORequest = VoteFixture.AddVote2();
    private String json;

    @Test
    @DisplayName("Happy Test: Should get Active Agendas")
    @SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.CLEAR_DB),
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
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.CLEAR_DB),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.INSERT_AGENDA),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.CLEAR_DB)
    })
    void getEndedAgendas() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agenda"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].question").value("Do you like leap years?"));
    }

    @Test
    @DisplayName("Happy Test: Should create an Agenda")
    @SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.CLEAR_DB),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.INSERT_USER),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.CLEAR_DB)
    })
    void postAgenda() throws Exception {
        
        json = mapper.writeValueAsString(agendaDTORequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/agenda")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(jsonPath("$.category").value("SPORTS"))
            .andExpect(jsonPath("$.question").value(agendaDTORequest.question()));
    }

    @Test
    @DisplayName("Happy Test: Should vote on Agenda")
    @SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.CLEAR_DB),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.INSERT_AGENDA),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.INSERT_USER),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.CLEAR_DB)
    })
    void voteOnAgenda() throws Exception {
        
        json = mapper.writeValueAsString(voteDTORequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/agenda/vote")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(jsonPath("$.userCpf").value(voteDTORequest.cpf()));

    }
}
