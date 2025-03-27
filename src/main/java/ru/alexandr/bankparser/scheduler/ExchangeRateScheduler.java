package ru.alexandr.bankparser.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.alexandr.bankparser.service.ExchangeRateSyncService;
import ru.alexandr.bankparser.util.Util;

import java.time.LocalDate;


@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateScheduler {

    private final ExchangeRateSyncService syncService;
    private final Util util;

    @Scheduled(cron = "${time.sync.schedule}")
    public void scheduledSync() {
        log.info("Запуск синхронизации курсов за день");
        syncService.syncRatesForDay(LocalDate.now().minusDays(1), util.getCurrencies());
    }
}
