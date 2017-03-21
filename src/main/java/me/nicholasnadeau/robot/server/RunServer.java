package me.nicholasnadeau.robot.server;

import me.nicholasnadeau.robot.communication.packet.PacketProtos.Packet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.UnknownHostException;

/**
 * Created on 2017-03-15.
 * <p>
 * Copyright Nicholas Nadeau 2017.
 */
public class RunServer {
    static private final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        logger.info("JVM version:\t{}", System.getProperty("java.version"));

        // start server
        final Server server = new Server("localhost", 1234);

        Thread serverThread = new Thread(server);
        serverThread.start();

        // wait until server is ready
        boolean isServerReady = false;
        while (!isServerReady) {
            if (server.getChannel() != null) {
                if (server.getChannel().isActive()) {
                    logger.info("Server is ready");
                    logger.info("Server channel bound to:\t{}", server.getChannel().localAddress());
                    isServerReady = true;
                }
            }
        }

        // start test
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!(server.getStatusGroup().size() > 0)) {

                }

                for (int i = 0; i < 100; i++) {
                    Packet packet = Packet.newBuilder().setCommandId(Packet.CommandID.KEEP_ALIVE).build();
                    logger.info("Publish:\t{}", i);
                    server.publish(packet);
                    try {
                        Thread.sleep(1);
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
