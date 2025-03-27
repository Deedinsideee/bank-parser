package ru.alexandr.bankparser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExchangeRateReportResponse {
    private String currency;
    private BigDecimal minRate;
    private BigDecimal maxRate;
    private BigDecimal avgRate;
}
