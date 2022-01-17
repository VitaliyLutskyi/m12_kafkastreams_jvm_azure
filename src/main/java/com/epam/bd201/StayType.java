package com.epam.bd201;

import lombok.Getter;

import static java.util.Objects.isNull;

@Getter
public enum StayType {
    ERRONEOUS_DATA("Erroneous data"),
    SHORT_STAY("Short stay"),
    STANDARD_STAY("Standard stay"),
    STANDARD_EXTENDED_STAY("Standard extended stay"),
    LONG_STAY("Long stay");

    final private String value;

    StayType(String value) {
        this.value = value;
    }

    public static StayType fromStayDuration(Integer stayDuration) {
        if (isNull(stayDuration) || stayDuration <= 0) return ERRONEOUS_DATA;
        if (stayDuration <= 4) return SHORT_STAY;
        if (stayDuration <= 10) return STANDARD_STAY;
        if (stayDuration <= 14) return STANDARD_EXTENDED_STAY;
        return LONG_STAY;
    }
}
