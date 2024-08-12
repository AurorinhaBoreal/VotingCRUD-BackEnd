package com.db.crud.voting.fixture;

import com.db.crud.voting.dto.request.AddVoteRequest;
import com.db.crud.voting.dto.response.AddVoteResponse;

public class VoteFixture {
    
    public static AddVoteRequest AddVote1() {
        return AddVoteRequest.builder()
            .cpf("05073122011")
            .question("I should bet on Palmeiras?")
            .vote("Y")
            .build();
    }

    public static AddVoteRequest AddVote2() {
        return AddVoteRequest.builder()
            .cpf("05073122011")
            .question("I should bet on Palmeiras?")
            .vote("N")
            .build();
    }

    public static AddVoteResponse AddVoteResponse() {
        return AddVoteResponse.builder()
            .userCpf("05073122011")
            .build();
    }
}
