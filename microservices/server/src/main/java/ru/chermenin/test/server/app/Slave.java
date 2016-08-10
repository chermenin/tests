package ru.chermenin.test.server.app;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Slave service application.
 *
 * @author chermenin
 */
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ComponentScan({"ru.chermenin.test.common", "ru.chermenin.test.slave"})
public class Slave {

}
