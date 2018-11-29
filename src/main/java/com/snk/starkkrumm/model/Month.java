package com.snk.starkkrumm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Month {
    JANUARY("01", "Ian"),
    FEBRUARY("02", "Feb"),
    MARCH("03", "Mar"),
    APRIL("04", "Apr"),
    MAY("05", "Mai"),
    JUNE("06", "Iun"),
    JULY("07", "Iul"),
    AUGUST("08", "Aug"),
    SEPTEMBER("09", "Sep"),
    OCTOBER("10", "Oct"),
    NOVEMBER("11", "Noi"),
    DECEMBER("12", "Dec");

    public String number;
    public String text;
}
