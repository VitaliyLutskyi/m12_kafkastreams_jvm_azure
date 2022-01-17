package com.epam.bd201;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static com.epam.bd201.StayType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class HotelVisitTest {

    @Test
    void notParsableDateTest() {
        HotelVisit visit = HotelVisit.builder()
                .srch_ci("20210124")
                .srch_co("2021-01-23")
                .build();
        visit.setStayDurationAndType();

        assertNull(visit.getStay_duration());
        assertEquals(ERRONEOUS_DATA.getValue(), visit.getStay_duration_type());
    }

    @Test
    void erroneousStayDurationTest() {
        HotelVisit visit = HotelVisit.builder()
                .srch_ci("2021-01-24")
                .srch_co("2021-01-23")
                .build();
        visit.setStayDurationAndType();

        assertEquals(-1, visit.getStay_duration());
        assertEquals(ERRONEOUS_DATA.getValue(), visit.getStay_duration_type());
    }

    @Test
    void shortStayDurationTest() {
        HotelVisit visit = HotelVisit.builder()
                .srch_ci("2021-01-22")
                .srch_co("2021-01-23")
                .build();
        visit.setStayDurationAndType();

        assertEquals(1, visit.getStay_duration());
        assertEquals(SHORT_STAY.getValue(), visit.getStay_duration_type());
    }

    @Test
    void standardStayDurationTest() {
        HotelVisit visit = HotelVisit.builder()
                .srch_ci("2021-12-25")
                .srch_co("2022-01-02")
                .build();
        visit.setStayDurationAndType();

        assertEquals(8, visit.getStay_duration());
        assertEquals(STANDARD_STAY.getValue(), visit.getStay_duration_type());
    }

    @Test
    void longStayDurationTest() {
        HotelVisit visit = HotelVisit.builder()
                .srch_ci("2021-12-25")
                .srch_co("2022-02-02")
                .build();
        visit.setStayDurationAndType();

        assertEquals(39, visit.getStay_duration());
        assertEquals(LONG_STAY.getValue(), visit.getStay_duration_type());
    }
}
