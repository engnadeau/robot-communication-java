package me.nicholasnadeau.communication.demo;

import me.nicholasnadeau.communication.client.Client;
import me.nicholasnadeau.robot.communication.packet.PacketProtos.Packet;

import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * Created on 2017-03-15.
 * <p>
 * Copyright Nicholas Nadeau 2017.
 */
public class RunClient {
    static private final Logger LOGGER = Logger.getLogger(RunClient.class.getSimpleName());

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        LOGGER.fine("JVM version:\t" + System.getProperty("java.version"));

        // start client
        final Client client = new Client("localhost", 1234);

        Thread thread = new Thread(client);
        thread.start();

        // start test
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < (10 * 1e3)) {
            Packet packet = client.getIncomingQueue().poll();
            if (packet != null) {
                LOGGER.info(String.valueOf(packet));
            }
        }

        // close
        client.close();
        thread.join();
    }
}
