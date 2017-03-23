package me.nicholasnadeau.communication.demo;

import me.nicholasnadeau.communication.server.Server;
import me.nicholasnadeau.robot.communication.packet.PacketProtos.Packet;

import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * Created on 2017-03-15.
 * <p>
 * Copyright Nicholas Nadeau 2017.
 */
public class RunServer {
    static private final Logger LOGGER = Logger.getLogger(RunServer.class.getSimpleName());

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        LOGGER.fine("JVM version:\t" + System.getProperty("java.version"));

        // start server
        final Server server = new Server("localhost", 1234);

        Thread serverThread = new Thread(server);
        serverThread.start();

        // wait until server is ready
        boolean isServerReady = false;
        while (!isServerReady) {
            if (server.getChannel() != null) {
                if (server.getChannel().isActive()) {
                    LOGGER.fine("Server is ready");
                    LOGGER.fine("Server channel bound to:\t" + server.getChannel().localAddress());
                    isServerReady = true;
                }
            }
        }

        // start test
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!(server.getChannelGroup().size() > 0)) {

                }

                for (int i = 0; i < 100; i++) {
                    Packet packet = Packet.newBuilder().setCommandId(Packet.CommandID.KEEP_ALIVE).build();
                    LOGGER.info("Publish:\t" + i);
                    server.publish(packet);
                    try {
                        Thread.sleep(99);
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
