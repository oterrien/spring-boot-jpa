package com.ote.test.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public abstract class Parameter<TE extends IEntity> {

    protected String sortingBy;

    @Convert(converter = SortDirectionConverter.class)
    protected Sort.Direction sortingDirection;

    public abstract Sort getSort();

    public abstract Specification<TE> getFilter();

    @Converter
    @NoArgsConstructor
    public static class SortDirectionConverter implements AttributeConverter<Sort.Direction, String> {

        @Override
        public String convertToDatabaseColumn(Sort.Direction direction) {
            return direction.name();
        }

        @Override
        public Sort.Direction convertToEntityAttribute(String directionString) {

            Optional<Sort.Direction> direction = Stream.
                    of(Sort.Direction.values()).
                    filter(p -> p.name().equalsIgnoreCase(directionString)).
                    findAny();

            return direction.orElse(Sort.Direction.ASC);
        }
    }
}
