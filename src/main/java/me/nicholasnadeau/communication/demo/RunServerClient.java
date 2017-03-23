package me.nicholasnadeau.communication.demo;

import java.net.UnknownHostException;
import java.util.logging.Logger;

public class RunServerClient {
    final private static Logger LOGGER = Logger.getLogger(RunServerClient.class.getSimpleName());

    public static void main(String[] args) {
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RunServer.main(null);
                } catch (UnknownHostException e) {
                    LOGGER.severe(String.valueOf(e));
                } catch (InterruptedException e) {
                    LOGGER.severe(String.valueOf(e));
                }
            }
        });

        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RunClient.main(null);
                } catch (UnknownHostException e) {
                    LOGGER.severe(String.valueOf(e));
                } catch (InterruptedException e) {
                    LOGGER.severe(String.valueOf(e));
                }
            }
        });

        try {
            serverThread.start();
            clientThread.start();
            clientThread.join();
        } catch (InterruptedException e) {
            LOGGER.severe(String.valueOf(e));
        }
    }
}
