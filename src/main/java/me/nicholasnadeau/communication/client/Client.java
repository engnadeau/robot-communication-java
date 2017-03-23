package me.nicholasnadeau.communication.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.nicholasnadeau.robot.communication.packet.PacketProtos;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class Client implements Runnable {
    static private final Logger logger = Logger.getLogger(Client.class.getSimpleName());
    private int port;
    private String host;
    private Channel channel;
    private EventLoopGroup eventLoopGroup;
    private Queue<PacketProtos.Packet> incomingQueue = new ConcurrentLinkedQueue<PacketProtos.Packet>();

    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public void run() {
        try {
            eventLoopGroup = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientInitializer(incomingQueue));

            // Make a new connection.
            channel = b.connect(host, port).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.severe(String.valueOf(e));
        } finally {
            close();
        }
    }

    public void close() {
        try {
            channel.close();
        } catch (NullPointerException e) {
            logger.severe(String.valueOf(e));
        }
        eventLoopGroup.shutdownGracefully();
    }

    public Queue<PacketProtos.Packet> getIncomingQueue() {
        return incomingQueue;
    }
}