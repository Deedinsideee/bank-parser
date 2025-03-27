package ru.alexandr.bankparser.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.alexandr.bankparser.model.ExchangeRate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CNBExchangeRateParser {

    private static final String DAILY_URL = "https://www.cnb.cz/en/financial_markets/foreign_exchange_market/exchange_rate_fixing/daily.txt?date=%s";
    private static final String YEARLY_URL = "https://www.cnb.cz/en/financial_markets/foreign_exchange_market/exchange_rate_fixing/year.txt?year=%s";

    public List<ExchangeRate> fetchDailyRates(LocalDate date) {
        String url = String.format(DAILY_URL, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        return fetchRates(url, date);
    }

    public List<ExchangeRate> fetchYearlyRates(int year) {
        String url = String.format(YEARLY_URL, year);
        return fetchRates(url, null);
    }

    private List<ExchangeRate> fetchRates(String url, LocalDate fixedDate) {
        try {
            String rawData = new BufferedReader(new InputStreamReader(new URL(url).openStream()))
                    .lines()
                    .reduce("", (acc, line) -> acc + "\n" + line);

            return parseExchangeRates(rawData, fixedDate);
        } catch (Exception e) {
            log.error("Ошибка загрузки данных с {}", url, e);
            return Collections.emptyList();
        }
    }

    private List<ExchangeRate> parseExchangeRates(String csvData, LocalDate fixedDate) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        LocalDate date = fixedDate;

        try (CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withDelimiter('|')
                .parse(new StringReader(csvData))) {

            int lineNumber = 0;
            for (CSVRecord record : parser) {
                lineNumber++;
                if (lineNumber <= 1) {
                    continue;
                }
                if (date == null) {
                    date = LocalDate.parse(record.get(0), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                }

                String currency = record.get(3);
                BigDecimal rate = new BigDecimal(record.get(4).replace(',', '.')); // Курс валюты

                exchangeRates.add(new ExchangeRate(null, currency, rate, date));
            }
        } catch (Exception e) {
            log.error("Ошибка парсинга данных", e);
        }

        return exchangeRates;
    }
}
