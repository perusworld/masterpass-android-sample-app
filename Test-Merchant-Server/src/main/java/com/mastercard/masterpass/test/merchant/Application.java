package com.mastercard.masterpass.test.merchant;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import org.apache.log4j.*;

import java.io.IOException;

/**
 * Application class that will be used to run server in IDE
 */
public class Application {

    private static final String PORT = "9998";

    /**
     * File/Console logger
     */
    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServerFactory.create("http://localhost:" + PORT + "/");
        server.start();

        logger.debug("Server running");
        logger.debug("Visit: http://localhost:" + PORT + "/version");
        logger.debug("Hit return to stop...");
        System.in.read();
        logger.debug("Stopping server");
        server.stop(0);
        logger.debug("Server stopped");
    }
}
