package com.ote.test.model;

import lombok.Data;

@Data
public class Clause {

    private String propertyToBeSorted;
    private String sortingDirection;

    private String propertyToBeFiltered;
    private String filteredValue;

}
