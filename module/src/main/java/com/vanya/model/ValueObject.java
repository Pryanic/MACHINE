package com.vanya.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ValueObject {

    private String name;
    private boolean isBig;
    private int age;

}
