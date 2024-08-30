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
import com.db.crud.voting.dto.request.UserRequest;
import com.db.crud.voting.fixture.SqlProvider;
import com.db.crud.voting.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(classes = {VotingApplication.class})
@ActiveProfiles("test")
class UserControllerIT {
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private UserRequest userRequest = UserFixture.UserDTOValid();
    private String json;

    @Test
    @DisplayName("Happy Test: Should get Specific")
    @SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.CLEAR_DB),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.INSERT_USER),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.CLEAR_DB)
    })
    void getSpecificUser() throws Exception {

        json = "33092209079";

        mockMvc.perform(MockMvcRequestBuilders.get("/user/specific")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userType").value("ADMIN"))
            .andExpect(jsonPath("$.firstName").value("Roberto"))
            .andExpect(jsonPath("$.surname").value("Carlos")); 
    }

    @Test
    @DisplayName("Happy Test: Should create User")
    @SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.CLEAR_DB),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = SqlProvider.INSERT_USER),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = SqlProvider.CLEAR_DB)
    })
    void postUser() throws Exception {

        json = mapper.writeValueAsString(userRequest);
        
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.userType").value("ADMIN"))
            .andExpect(jsonPath("$.firstName").value("Aurora"))
            .andExpect(jsonPath("$.surname").value("Kruschewsky")); 
    }
}
