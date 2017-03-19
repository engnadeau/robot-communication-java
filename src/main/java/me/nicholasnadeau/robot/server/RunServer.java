package me.nicholasnadeau.robot.server;

import io.netty.handler.logging.LoggingHandler;
import me.nicholasnadeau.robot.communication.packet.PacketProtos;
import me.nicholasnadeau.robot.communication.packet.PacketProtos.Packet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Condition;
import java.util.logging.Logger;

/**
 * Created on 2017-03-15.
 * <p>
 * Copyright Nicholas Nadeau 2017.
 */
public class RunServer {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        final String LOGGER_NAME = LoggingHandler.class.getName();
        final Logger logger = Logger.getLogger(LOGGER_NAME);

        System.out.println(String.format("JVM version:\t%s", System.getProperty("java.version")));

        // start server
        final Server server = new Server("localhost", 1234);

        Thread serverThread = new Thread(server);
        serverThread.start();

        // wait until server is ready
        boolean isServerReady = false;
        while (!isServerReady) {
            if (server.getChannel() != null) {
                if (server.getChannel().isActive()) {
                    System.out.println("Server is ready");
                    System.out.println(String.format("Server channel bound to:\t%s", server.getChannel().localAddress()));
                    isServerReady = true;
                }
            }
        }

        // start test
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    Packet packet = Packet.newBuilder().setCommandId(Packet.CommandID.KEEP_ALIVE).build();
                    System.out.println(String.format("Publish:\t%d", i));
                    server.publish(packet);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        testThread.start();
        testThread.join();

        // close
        server.close();
        serverThread.join();


    }
}
