package com.test.jc53325.service;

import com.test.jc53325.entity.CountryHoliday;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HolidayService {

    public static final String CSV_FILE_PATH = "holiday-dummy.csv";

    public boolean saveHoliday(CountryHoliday countryHoliday) {
        // read csv file holiday-dummy.csv
        List<CountryHoliday> countryHolidays = readCsvFile();
        // add data and distinct by countryCode and holidayDate
        if(!countryHolidays.contains(countryHoliday)) {
            countryHolidays.add(countryHoliday);
        }
        // write the filtered records back to a new CSV file or overwrite the existing one
        return write(countryHolidays);
    }

    public boolean saveHolidays(List<CountryHoliday> addCountryHolidays) {
        // read csv file holiday-dummy.csv
        List<CountryHoliday> existingCountryHolidays = readCsvFile();
        // add data and distinct by countryCode and holidayDate
        List<CountryHoliday> newCountryHolidays = new ArrayList<>(existingCountryHolidays);
        for(CountryHoliday countryHoliday : addCountryHolidays) {
            if(!existingCountryHolidays.contains(countryHoliday)) {
                newCountryHolidays.add(countryHoliday);
            }
        }
        // write the filtered records back to a new CSV file or overwrite the existing one
        return write(newCountryHolidays);
    }

    public boolean updateHolidays(List<CountryHoliday> updateCountryHolidays) {
        // read csv file holiday-dummy.csv
        List<CountryHoliday> countryHolidays = readCsvFile();
        // updatedHoliday
        List<CountryHoliday> updatedCountryHolidays = new ArrayList<>();
        // check if exists countryCode and holidayDate
        for(CountryHoliday updateCountryHoliday : updateCountryHolidays) {
            // update holidayName
            countryHolidays.stream().
                    filter(countryHoliday -> {
                        if(countryHoliday.getCountryCode().equals(updateCountryHoliday.getCountryCode())
                            && countryHoliday.getHolidayDate().equals(updateCountryHoliday.getHolidayDate())){
                            updatedCountryHolidays.add(countryHoliday);
                            return true;
                        };
                        return false;
                    })
                    .forEach(countryHoliday -> {
                        countryHoliday.setHolidayName(updateCountryHoliday.getHolidayName());
                        countryHoliday.setCountryDesc(updateCountryHoliday.getCountryDesc());
                    });
        }
        if(updatedCountryHolidays.isEmpty()){
            return false;
        }
        // write the filtered records back to a new CSV file or overwrite the existing one
        return write(countryHolidays);
    }

    public boolean removeHoliday(CountryHoliday removeHoliday){
        // read csv file holiday-dummy.csv
        List<CountryHoliday> countryHolidays = readCsvFile();
        // filter by countryCode and holidayDate
        List<CountryHoliday> filteredRecords = countryHolidays.stream()
                .filter(countryHoliday -> !countryHoliday.getCountryCode().equals(removeHoliday.getCountryCode())
                        || !countryHoliday.getHolidayDate().equals(removeHoliday.getHolidayDate()))
              .collect(Collectors.toList());
        // write the filtered records back to a new CSV file or overwrite the existing one
        return write(filteredRecords);
    }

    public void removeHolidays(List<CountryHoliday> removeHolidays){
        // remove multiple holidays
        for(CountryHoliday removeHoliday : removeHolidays) {
            removeHoliday(removeHoliday);
        }
    }

    public List<CountryHoliday> getNextYearHolidays(String countryCode){
        // 1. read csv file holiday-dummy.csv
        List<CountryHoliday> countryHolidays = readCsvFile();
        // 2. filter by countryCode and holidayDate > now() and holidayDate < now() + 1 year
        return countryHolidays.stream().filter(countryHoliday -> countryHoliday.getCountryCode().equals(countryCode)
                        && countryHoliday.getHolidayDate().isAfter(LocalDate.now().plusYears(1).minusDays(1))
                        && countryHoliday.getHolidayDate().isBefore(LocalDate.now().plusYears(2).minusDays(1)))
                .collect(Collectors.toList());
    }


    public CountryHoliday nextHoliday(String countryCode) {
        // 1. read csv file holiday-dummy.csv
        List<CountryHoliday> countryHolidays = readCsvFile();
        // 2. filter by countryCode and holidayDate > now()
        return countryHolidays.stream().filter(countryHoliday -> countryHoliday.getCountryCode().equals(countryCode) && countryHoliday.getHolidayDate().isAfter(LocalDate.now()))
                .min(Comparator.comparing(CountryHoliday::getHolidayDate)).orElse(null);

    }

    public Map<String, Boolean> isHoliday(LocalDate date){
        // 1. read csv file holiday-dummy.csv
        List<CountryHoliday> countryHolidays = readCsvFile();
        // 2. filter by holidayDate = date
        return countryHolidays.stream()
                .collect(Collectors.toMap(CountryHoliday::getCountryCode, countryHoliday -> countryHoliday.getHolidayDate().equals(date)));
    }

    private List<CountryHoliday> readCsvFile(){
        List<CountryHoliday> countryHolidays = new ArrayList<>();
        try (FileReader reader = new FileReader(HolidayService.class.getClassLoader().getResource(HolidayService.CSV_FILE_PATH).getFile());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                String countryCode = csvRecord.get("Country Code");
                String countryDesc = csvRecord.get("Country Description");
                String holidayDate = csvRecord.get("Holiday Date");
                String holidayName = csvRecord.get("Holiday Name");
                CountryHoliday countryHoliday = new CountryHoliday();
                countryHoliday.setCountryCode(countryCode);
                countryHoliday.setCountryDesc(countryDesc);
                countryHoliday.setHolidayDate(LocalDate.parse(holidayDate));
                countryHoliday.setHolidayName(holidayName);
                countryHolidays.add(countryHoliday);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return countryHolidays;
    }


    private boolean write(List<CountryHoliday> countryHolidays){
        // Write the filtered records back to a new CSV file or overwrite the existing one
        try (FileWriter writer = new FileWriter(Objects.requireNonNull(HolidayService.class.getClassLoader().getResource(HolidayService.CSV_FILE_PATH)).getFile());
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Country Code", "Country Description", "Holiday Date", "Holiday Name"))) {

            for (CountryHoliday record : countryHolidays) {
                // parse record to csvRecord
                csvPrinter.printRecord(record.getCountryCode(), record.getCountryDesc(), record.getHolidayDate(), record.getHolidayName());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
