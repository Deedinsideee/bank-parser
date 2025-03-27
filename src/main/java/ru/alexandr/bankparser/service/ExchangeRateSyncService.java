package ru.alexandr.bankparser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.bankparser.model.ExchangeRate;
import ru.alexandr.bankparser.parser.CNBExchangeRateParser;
import ru.alexandr.bankparser.repository.ExchangeRateRepository;
import ru.alexandr.bankparser.util.Util;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateSyncService {

    private final CNBExchangeRateParser parser;
    private final Util util;
    private final ExchangeRateRepository repository;

    public void syncRatesForDay(LocalDate date, List<String> currencies) {
            try {
                List<ExchangeRate> rates = parser.fetchDailyRates(date); // Запрос к API
                List<ExchangeRate> filteredRates = rates.stream()
                        .filter(rate -> currencies.contains(rate.getCurrency()))
                        .toList();

                repository.saveAll(filteredRates);
                log.info("Синхронизация завершена для {} и для валют : {}", date, currencies);
            } catch (Exception e) {
                log.error("Ошибка синхронизации для {}: {}", date, e.getMessage());
            }

    }


    public void syncRatesForPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            try {
                List<ExchangeRate> rates = parser.fetchDailyRates(date); // Запрос к API
                List<ExchangeRate> filteredRates = rates.stream()
                        .filter(rate -> util.getCurrencies().contains(rate.getCurrency()))
                        .toList();

                repository.saveAll(filteredRates);
                log.info("Синхронизация завершена для {}", date);
            } catch (Exception e) {
                log.error("Ошибка синхронизации для {}: {}", date, e.getMessage());
            }
            date = date.plusDays(1);
        }
    }


}
