package com.db.crud.voting.fixture;

import com.db.crud.voting.dto.request.AddVoteRequest;

public class VoteFixture {
    
    public static AddVoteRequest AddVote1() {
        return AddVoteRequest.builder()
            .cpf("05073122011")
            .question("I should bet on Palmeiras?")
            .yes(true)
            .no(false)
            .build();
    }

    public static AddVoteRequest AddVote2() {
        return AddVoteRequest.builder()
            .cpf("05073122011")
            .question("I should bet on Palmeiras?")
            .yes(false)
            .no(true)
            .build();
    }
}
