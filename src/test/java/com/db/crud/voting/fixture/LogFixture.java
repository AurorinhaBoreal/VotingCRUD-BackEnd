package com.db.crud.voting.fixture;

import java.time.LocalDateTime;

import com.db.crud.voting.dto.request.LogObj;
import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.model.Log;

public class LogFixture {

    public static LogObj LogObjEntityValid1() {
        return LogObj.builder()
            .objId(1L)
            .objInfo("I Should bet on Palmeiras?")
            .objType("Agenda")
            .operation(Operation.CREATE)
            .realizedOn(LocalDateTime.now())
            .build();
    }
    
    public static Log LogEntityValid1() {
        return Log.builder()
            .id(1L)
            .objectId(2l)
            .objectInfo("Aurora Kruschewsky")
            .objectType("User")
            .operation(Operation.CREATE)
            .realizedOn(LocalDateTime.now())
            .build();
    }

    public static Log LogEntityValid2() {
        return Log.builder()
            .id(2L)
            .objectId(3l)
            .objectInfo("Should I make Brigadeiro?")
            .objectType("Agenda")
            .operation(Operation.CREATE)
            .realizedOn(LocalDateTime.now())
            .build();
    }
}
