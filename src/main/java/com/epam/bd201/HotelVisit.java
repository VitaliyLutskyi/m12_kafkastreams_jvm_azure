package com.epam.bd201;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelVisit {
    private Long id;
    private String date_time;
    private Integer site_name;
    private Integer posa_container;
    private Integer user_location_country;
    private Integer user_location_region;
    private Integer user_location_city;
    private Double orig_destination_distance;
    private Integer user_id;
    private Integer is_mobile;
    private Integer is_package;
    private Integer channel;
    private String srch_ci;
    private String srch_co;
    private Integer srch_adults_cnt;
    private Integer srch_children_cnt;
    private Integer srch_rm_cnt;
    private Integer srch_destination_id;
    private Integer srch_destination_type_id;
    private Long hotel_id;

    // fields to be derived from srch_ci, srch_o
    private Integer stay_duration;
    private String stay_duration_type;

    public HotelVisit setStayDurationAndType() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Integer stayDuration = null;
        try {
            LocalDate dateIn = LocalDate.parse(srch_ci, dateFormatter);
            LocalDate dateOut = LocalDate.parse(srch_co, dateFormatter);
            stayDuration = (int) ChronoUnit.DAYS.between(dateIn, dateOut);
        } catch (DateTimeParseException e) {
            System.out.println(e);
        }

        this.stay_duration = stayDuration;
        this.stay_duration_type = StayType.fromStayDuration(stayDuration).getValue();
        return this;
    }
}
