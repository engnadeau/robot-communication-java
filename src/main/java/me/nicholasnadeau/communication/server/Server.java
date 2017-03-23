package me.nicholasnadeau.communication.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import me.nicholasnadeau.robot.communication.packet.PacketProtos.Packet;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Created on 2017-03-15.
 * <p>
 * Copyright Nicholas Nadeau 2017.
 */
public class Server implements Runnable {
    static private final Logger logger = Logger.getLogger(Server.class.getSimpleName());
    private Channel channel;
    private InetSocketAddress inetSocketAddress;
    private EventLoopGroup workerGroup;
    private EventLoopGroup bossGroup;
    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private Queue<Packet> incomingQueue = new ConcurrentLinkedQueue<Packet>();

    public Server(String host, int port) {
        this.inetSocketAddress = new InetSocketAddress(host, port);
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public int getPort() {
        return inetSocketAddress.getPort();
    }

    @Override
    public void run() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ServerInitializer(channelGroup, incomingQueue));

            channel = bootstrap.bind(inetSocketAddress).sync().channel();

            // await until channel in closed
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
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public Channel getChannel() {
        return channel;
    }

    public void publish(Packet packet) {
        logger.fine("Publishing to:\t" + channelGroup);
        channelGroup.writeAndFlush(packet);
    }

    public Queue<Packet> getIncomingQueue() {
        return incomingQueue;
    }
}
