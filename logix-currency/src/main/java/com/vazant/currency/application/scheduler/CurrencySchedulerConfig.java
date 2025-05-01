package com.vazant.currency.application.scheduler;

import com.vazant.currency.infrastructure.config.CurrencyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
@RequiredArgsConstructor
public class CurrencySchedulerConfig implements SchedulingConfigurer {

  private final CurrencyRatesUpdater updater;
  private final CurrencyProperties properties;

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    CronTrigger cron = new CronTrigger(properties.getScheduleCron());
    taskRegistrar.addTriggerTask(updater::updateRates, cron);
  }
}
