package com.db.crud.voting.fixture;

import com.db.crud.voting.dto.request.AddVoteRequest;
import com.db.crud.voting.dto.response.AddVoteResponse;
import com.db.crud.voting.enums.Vote;

public class VoteFixture {
    
    public static AddVoteRequest AddVote1() {
        return AddVoteRequest.builder()
            .cpf("05073122011")
            .question("I should bet on Palmeiras?")
            .vote(Vote.YES)
            .build();
    }

    public static AddVoteRequest AddVote2() {
        return AddVoteRequest.builder()
            .cpf("33092209079")
            .question("Do you like Air-Fryers?")
            .vote(Vote.NO)
            .build();
    }

    public static AddVoteResponse AddVoteResponse() {
        return AddVoteResponse.builder()
            .userCpf("05073122011")
            .build();
    }
}
