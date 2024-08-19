package com.db.crud.voting.fixture;

import com.db.crud.voting.dto.request.VoteRequest;
import com.db.crud.voting.dto.response.VoteResponse;
import com.db.crud.voting.enums.Vote;

public class VoteFixture {
    
    public static VoteRequest AddVote1() {
        return VoteRequest.builder()
            .cpf("05073122011")
            .question("I should bet on Palmeiras?")
            .vote(Vote.YES)
            .build();
    }

    public static VoteRequest AddVote2() {
        return VoteRequest.builder()
            .cpf("33092209079")
            .question("Do you like Air-Fryers?")
            .vote(Vote.NO)
            .build();
    }

    public static VoteResponse AddVoteResponse() {
        return VoteResponse.builder()
            .userCpf("05073122011")
            .build();
    }
}
