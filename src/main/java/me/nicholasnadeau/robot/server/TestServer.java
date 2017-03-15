package me.nicholasnadeau.robot.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created on 2017-03-15.
 * <p>
 * Copyright Nicholas Nadeau 2017.
 */
public class TestServer {
    public static void main(String[] args) throws UnknownHostException {
        System.out.println(String.format("JVM version:\t%s", System.getProperty("java.version")));

//        String host = InetAddress.getLocalHost().getHostName();
        String host = "localhost";
        System.out.println(String.format("Host:\t%s", host));

        int port = 1234;
        System.out.println(String.format("Port:\t%d", port));

        Server server = new Server(host, port);
        server.run();
    }
}
