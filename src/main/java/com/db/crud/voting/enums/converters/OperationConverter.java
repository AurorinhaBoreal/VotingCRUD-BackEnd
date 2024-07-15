package com.db.crud.voting.enums.converters;

import java.util.stream.Stream;

import com.db.crud.voting.enums.Operation;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OperationConverter implements AttributeConverter<Operation, String> {

    @Override
    public String convertToDatabaseColumn(Operation operation) {
        if (operation == null) {
            return null;
        }
        return operation.getCode();
    }

    @Override
    public Operation convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Operation.values())
                .filter(o -> o.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
