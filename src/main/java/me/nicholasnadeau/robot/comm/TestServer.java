package me.nicholasnadeau.robot.comm;

/**
 * Created on 2017-03-15.
 * <p>
 * Copyright Nicholas Nadeau 2017.
 */
public class TestServer {
    public static void main(String[] args) {
        System.out.println(System.getProperty("java.version"));

        Server server = new Server(1234);
        server.run();
    }
}
