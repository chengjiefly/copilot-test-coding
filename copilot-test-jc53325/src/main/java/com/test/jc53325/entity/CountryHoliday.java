package com.test.jc53325.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class CountryHoliday {
    @NotNull
    private String countryCode;

    @NotNull
    private String countryDesc;

    @NotNull
    private LocalDate holidayDate;

    @NotNull
    private String holidayName;

    // getters and setters for countryCode, countryDesc, holidayDate, holidayName

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryDesc() {
        return countryDesc;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setCountryDesc(String countryDesc) {
        this.countryDesc = countryDesc;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    @Override
    public String toString() {
        return "CountryHoliday{" +
                "countryCode='" + countryCode + '\'' +
                ", countryDesc='" + countryDesc + '\'' +
                ", holidayDate=" + holidayDate +
                ", holidayName='" + holidayName + '\'' +
                '}';
    }

    // equals by countryCode and HolidayDate
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(!(obj instanceof CountryHoliday)) {
            return false;
        }
        CountryHoliday countryHoliday = (CountryHoliday) obj;
        return this.countryCode.equals(countryHoliday.countryCode) && this.holidayDate.equals(countryHoliday.holidayDate);
    }


}
