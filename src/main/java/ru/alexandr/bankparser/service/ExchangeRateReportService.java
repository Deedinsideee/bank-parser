package ru.alexandr.bankparser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alexandr.bankparser.dto.ExchangeRateReportResponse;
import ru.alexandr.bankparser.model.ExchangeRate;
import ru.alexandr.bankparser.repository.ExchangeRateRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeRateReportService {

    private final ExchangeRateRepository repository;

    public List<ExchangeRateReportResponse> generateReport(LocalDate startDate, LocalDate endDate, List<String> currencies) {
        List<ExchangeRate> rates = repository.findByCurrencyInAndDateBetween(currencies, startDate, endDate);

        return rates.stream()
                .collect(Collectors.groupingBy(ExchangeRate::getCurrency))
                .entrySet()
                .stream()
                .map(entry -> {
                    List<BigDecimal> values = entry.getValue().stream()
                            .map(ExchangeRate::getRate)
                            .toList();

                    return new ExchangeRateReportResponse(
                            entry.getKey(),
                            values.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO),
                            values.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO),
                            values.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                                    .divide(BigDecimal.valueOf(values.size()), RoundingMode.HALF_UP)
                    );
                })
                .toList();
    }
}
