package com.ote.test.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PersonParameter extends Parameter<Person> {

    private String firstName;
    private String lastName;

    public Sort getSort() {

        Sort.Order orderByPrimaryKeyAsc = new Sort.Order(Sort.Direction.ASC, "key.id");

        if (sortingBy == null) {
            return new Sort(orderByPrimaryKeyAsc);
        }

        Sort.Order orderByPropertyAndDirection = new Sort.Order(sortingDirection, sortingBy);

        if ("id".equalsIgnoreCase(sortingBy)) {
            return new Sort(orderByPropertyAndDirection);
        }

        return new Sort(orderByPropertyAndDirection, orderByPrimaryKeyAsc);
    }

    public Specification<Person> getFilter() {

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
