package com.db.crud.voting.fixture;

import java.time.LocalDateTime;

import com.db.crud.voting.enums.Operation;
import com.db.crud.voting.enums.converters.OperationConverter;
import com.db.crud.voting.model.Log;

public class LogFixture {
    
    static OperationConverter operationConverter = new OperationConverter();

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
