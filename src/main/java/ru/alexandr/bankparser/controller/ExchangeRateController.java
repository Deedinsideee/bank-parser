package ru.alexandr.bankparser.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alexandr.bankparser.dto.ExchangeRateReportRequest;
import ru.alexandr.bankparser.dto.ExchangeRateReportResponse;
import ru.alexandr.bankparser.scheduler.ExchangeRateScheduler;
import ru.alexandr.bankparser.service.ExchangeRateReportService;
import ru.alexandr.bankparser.service.ExchangeRateSyncService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateController {

    private final ExchangeRateSyncService syncService;
    private final ExchangeRateReportService reportService;
    private final ExchangeRateScheduler scheduler;
// Ручка для запуска синхронизации за день
    @GetMapping("/sync/daily")
    public ResponseEntity<String> syncDaily() {
        scheduler.scheduledSync();
        return ResponseEntity.ok("Синхронизация за вчерашнюю дату завершена");
    }

    @PostMapping("/sync/period")
    public ResponseEntity<String> syncForPeriod(@RequestBody ExchangeRateReportRequest request) {
        log.info("Синхронизация начата для периода с {} по {} и для валют : {}", request.getStartDate(), request.getEndDate(), request.getCurrencies());
        syncService.syncRatesForPeriod(request.getStartDate(), request.getEndDate(), request.getCurrencies());
        log.info("Синхронизация завершена");
        return ResponseEntity.ok("Синхронизация за период " + request.getStartDate() + " - " + request.getEndDate() + " завершена");
    }

    @PostMapping("/report")
    public ResponseEntity<List<ExchangeRateReportResponse>> getReport(@RequestBody ExchangeRateReportRequest request) {
        return ResponseEntity.ok(reportService.generateReport(request.getStartDate(), request.getEndDate(), request.getCurrencies()));
    }
}
