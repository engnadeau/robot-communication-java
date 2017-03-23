package me.nicholasnadeau.communication;

import me.nicholasnadeau.communication.client.Client;
import me.nicholasnadeau.communication.server.Server;
import me.nicholasnadeau.robot.communication.packet.PacketProtos;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ServerClientTest {
    final private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    int testTime = 5 * 1000;   // ms
    int numPackets = 50;
    int packetSendPeriod = testTime / 50;    // ms

    @Test
    public void serverClientShouldRun() {
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runServer();
            }
        });
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runClient();
            }
        });

        serverThread.start();
        clientThread.start();

        try {
            clientThread.join();
        } catch (InterruptedException e) {
            Assert.fail(String.valueOf(e));
        }
    }

    private void runClient() {
        final Client client = new Client("localhost", 1234);

        Thread thread = new Thread(client);
        thread.start();

        // start test
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < (testTime)) {
            PacketProtos.Packet packet = client.getIncomingQueue().poll();
            if (packet != null) {
                logger.info(String.valueOf(packet));
            }
        }

        // close
        client.close();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Assert.fail(String.valueOf(e));
        }
    }

    private void runServer() {
        // start server
        final Server server = new Server("localhost", 1234);

        Thread serverThread = new Thread(server);
        serverThread.start();

        // wait until server is ready
        boolean isServerReady = false;
        while (!isServerReady) {
            if (server.getChannel() != null) {
                if (server.getChannel().isActive()) {
                    logger.fine("Server is ready");
                    logger.fine("Server channel bound to:\t" + server.getChannel().localAddress());
                    isServerReady = true;
                }
            }
        }

        // start test
        boolean isClientConnected = false;
        while (!isClientConnected) {
            if (server.getChannelGroup().size() > 0) {
                isClientConnected = true;
            }
        }

        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                sendPackets(server);
            }
        });

        testThread.start();
        try {
            testThread.join();
        } catch (InterruptedException e) {
            Assert.fail(String.valueOf(e));
        }

        // close
        server.close();
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            Assert.fail(String.valueOf(e));
        }
    }

    private void sendPackets(Server server) {
        for (int i = 0; i < numPackets; i++) {
            PacketProtos.Packet packet = PacketProtos.Packet.newBuilder().setCommandId(PacketProtos.Packet.CommandID.KEEP_ALIVE).build();
            logger.info("Publish:\t" + i);
            server.publish(packet);
            try {
                Thread.sleep(packetSendPeriod);
            } catch (InterruptedException e) {
                Assert.fail(String.valueOf(e));
            }
        }
    }
}
