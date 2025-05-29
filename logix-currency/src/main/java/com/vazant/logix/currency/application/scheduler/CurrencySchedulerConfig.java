package com.vazant.logix.currency.application.scheduler;

import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class CurrencySchedulerConfig implements SchedulingConfigurer {

  private final CurrencyRatesUpdater updater;
  private final CurrencyProperties properties;

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    log.info("⏰ Обновление курсов валют по расписанию...");
    CronTrigger cron = new CronTrigger(properties.getScheduleCron());
    taskRegistrar.addTriggerTask(updater::updateRates, cron);
  }
}
