package com.vazant.logix.currency.application.scheduler;

import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

/**
 * Scheduler configuration for periodic currency rates update.
 * <p>
 * Uses CronTrigger and parameters from CurrencyProperties to schedule updates.
 */
@Configuration
@EnableScheduling
public class CurrencySchedulerConfig implements SchedulingConfigurer {

  private static final Logger log = LoggerFactory.getLogger(CurrencySchedulerConfig.class);

  private final CurrencyRatesUpdater updater;
  private final CurrencyProperties properties;

  /**
   * Constructs a new CurrencySchedulerConfig.
   *
   * @param updater service for updating currency rates
   * @param properties currency service properties
   */
  public CurrencySchedulerConfig(CurrencyRatesUpdater updater, CurrencyProperties properties) {
    this.updater = updater;
    this.properties = properties;
  }

  /**
   * Configures scheduled tasks for updating currency rates using a cron expression.
   *
   * @param taskRegistrar the task registrar to configure
   */
  @Override
  public void configureTasks(@NonNull ScheduledTaskRegistrar taskRegistrar) {
    log.info("⏰ Обновление курсов валют по расписанию...");
    CronTrigger cron = new CronTrigger(properties.getScheduleCron());
    taskRegistrar.addTriggerTask(updater::updateRates, cron);
  }
}
