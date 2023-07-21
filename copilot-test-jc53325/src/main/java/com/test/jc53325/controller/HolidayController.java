package com.test.jc53325.controller;

import com.test.jc53325.entity.CountryHoliday;
import com.test.jc53325.service.HolidayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class HolidayController {
    @Resource
    private HolidayService holidayService;

    @PostMapping("/addNewHolidays")
    public String addNewHolidays(@RequestBody List<CountryHoliday> countryHolidays) {
        // add new holiday controller here
        holidayService.saveHolidays(countryHolidays);
        return "add success";
    }

    @PostMapping("/updateHolidays")
    public String updateHolidays(@RequestBody List<CountryHoliday> countryHolidays) {
        // update holiday controller here
        if(holidayService.updateHolidays(countryHolidays)){
            return "update success:" + countryHolidays;
        }
        return "No record found to update";
    }

    @PostMapping("/deleteHolidays")
    public String deleteHolidays(@RequestBody List<CountryHoliday> countryHolidays) {
        // delete holiday controller here
        holidayService.removeHolidays(countryHolidays);
        return "delete success";
    }

    @GetMapping("/queryNextYearHolidaysOnCountry")
    public List<CountryHoliday> queryNextYearHolidaysOnCountry(String countryCode) {
        // query next year holidays controller here
        return holidayService.getNextYearHolidays(countryCode);
    }

    @GetMapping("/queryNextHolidayOnCountry")
    public CountryHoliday queryNextHolidayOnCountry(String countryCode) {
        // query next holiday on country controller here
        return holidayService.nextHoliday(countryCode);
    }

    @GetMapping("/checkIsHolidayOnDate")
    public Map<String, Boolean> checkIsHolidayOnDate(String date) {
        // check is holiday on country controller here
        return holidayService.isHoliday(LocalDate.parse(date));
    }
}
