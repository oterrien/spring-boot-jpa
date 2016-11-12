package com.ote.test.model;

import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PersonParameter {

    private String sortingBy;

    // TODO Add JSon converter to convert to enum
    private String sortingDirection;

    private String firstName;
    private String lastName;

    public Sort sort() {

        Sort.Order orderByPrimaryKeyAsc = new Sort.Order(Sort.Direction.ASC, "id");

        if (sortingBy == null) {
            return new Sort(orderByPrimaryKeyAsc);
        }

        if (sortingDirection == null) {
            sortingDirection = "ASC";
        }

        Sort.Direction direction = Sort.Direction.valueOf(sortingDirection.toUpperCase());
        Sort.Order orderByPropertyAndDirection = new Sort.Order(direction, sortingBy);

        if ("id".equalsIgnoreCase(sortingBy)){
            return new Sort(orderByPropertyAndDirection);
        }

        return new Sort(orderByPropertyAndDirection, orderByPrimaryKeyAsc);
    }

    public Specification<Person> filter() {

        return (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (firstName != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("firstName")), firstName.toUpperCase()));
            }

            if (lastName != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("lastName")), lastName.toUpperCase()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }


}
