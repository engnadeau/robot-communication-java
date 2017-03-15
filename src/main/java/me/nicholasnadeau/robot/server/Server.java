package me.nicholasnadeau.robot.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created on 2017-03-15.
 * <p>
 * Copyright Nicholas Nadeau 2017.
 */
public class Server implements Runnable {
    EventLoopGroup eventLoopGroup;
    private int port;
    private String host;


    public Server(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void run() {
        eventLoopGroup = new NioEventLoopGroup(1);

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ServerInitializer());

            ChannelFuture f = bootstrap.bind(this.getHost(), this.getPort()).sync();
            System.out.println(String.format("Server bound to:\t%s", f.channel().localAddress()));
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void close() {
        // Shut down all event loops to terminate all threads.
        eventLoopGroup.shutdownGracefully();
    }

    public String getHost() {
        return host;
    }
}
