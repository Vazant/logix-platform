package com.vazant.logix.devtools;

import com.vazant.logix.devtools.container.*;
import com.vazant.logix.devtools.config.DevContainerProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(DevContainerProperties.class)
@Import({PostgresDev.class, KafkaDev.class, ElasticsearchDev.class})
public class DevtoolsAutoConfiguration {
}
