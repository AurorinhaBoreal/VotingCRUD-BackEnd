package com.db.crud.voting.enums.converters;

import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.db.crud.voting.enums.Category;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@Component
public class CategoryConverter implements AttributeConverter<Category, String> {

    @Override
    public String convertToDatabaseColumn(Category category) {
        if (category == null) {
            return null;
        }
        return category.getCode();
    }

    @Override
    public Category convertToEntityAttribute(String cat) {
        if (cat == null) {
            return null;
        }

        return Stream.of(Category.values())
                .filter(c -> c.getCode().equals(cat))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
