package ru.chermenin.test.server.app;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Master service application.
 *
 * @author chermenin
 */
@EnableScheduling
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ComponentScan({"ru.chermenin.test.common", "ru.chermenin.test.master"})
public class Master {

}
