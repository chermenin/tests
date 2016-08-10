package ru.chermenin.test.server;

import org.springframework.boot.SpringApplication;
import ru.chermenin.test.server.app.Master;
import ru.chermenin.test.server.app.Slave;

import java.util.stream.Stream;

/**
 * General server launcher class.
 *
 * @author chermenin
 */
public class Launcher {

    private final static String PORT_ARG = "--port=";
    private final static String MASTER_ARG = "--master";
    private final static String SLAVE_ARG = "--slave";

    public static void main(String[] args) {
        int port = Stream.of(args)
                .filter(s -> s.startsWith(PORT_ARG))
                .map(s -> s.substring(PORT_ARG.length()))
                .mapToInt(Integer::parseInt)
                .findFirst().orElse(8080);

        System.setProperty("server.port", String.valueOf(port));

        boolean isMaster = Stream.of(args).anyMatch(s -> s.equalsIgnoreCase(MASTER_ARG));
        boolean isSlave = Stream.of(args).anyMatch(s -> s.equalsIgnoreCase(SLAVE_ARG));

        if (isMaster == isSlave) {
            printUsage();
        } else {
            SpringApplication.run(isMaster ? Master.class : Slave.class, args);
        }
    }

    private static void printUsage() {
        System.out.println("" +
                "Select mode to master or slave.\n\n" +
                "Master mode:\n" +
                "\tserver --master [--port=8080] --slavePorts=8081[,8082,...]\n\n" +
                "Slave mode:\n" +
                "\tserver --slave [--port=8080]\n\n" +
                "Default port in both modes is 8080."
        );
    }
}
