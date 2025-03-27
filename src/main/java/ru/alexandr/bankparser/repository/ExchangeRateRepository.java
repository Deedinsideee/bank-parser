package ru.alexandr.bankparser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alexandr.bankparser.model.ExchangeRate;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    List<ExchangeRate> findByCurrencyInAndDateBetween(List<String> currencies, LocalDate start, LocalDate end);
}