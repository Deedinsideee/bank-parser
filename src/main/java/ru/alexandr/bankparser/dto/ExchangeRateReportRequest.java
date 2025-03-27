package ru.alexandr.bankparser.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExchangeRateReportRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> currencies; // Валюты, по которым нужен отчёт
}