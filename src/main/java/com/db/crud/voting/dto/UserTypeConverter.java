package com.db.crud.voting.dto;

import java.util.stream.Stream;

import com.db.crud.voting.enums.UserType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserType, String> {

    @Override
    public String convertToDatabaseColumn(UserType userType) {
        if (userType == null) {
            return null;
        }
        return userType.getCode();
    }

    @Override
    public UserType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(UserType.values())
                .filter(t -> t.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
