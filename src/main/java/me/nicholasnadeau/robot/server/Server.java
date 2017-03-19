package me.nicholasnadeau.robot.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import me.nicholasnadeau.robot.communication.packet.PacketProtos.Packet;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017-03-15.
 * <p>
 * Copyright Nicholas Nadeau 2017.
 */
public class Server implements Runnable {
    private Channel channel;
    private InetSocketAddress inetSocketAddress;
    private EventLoopGroup workerGroup;
    private EventLoopGroup bossGroup;
    private ChannelGroup statusGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public Server(String host, int port) {
        this.inetSocketAddress = new InetSocketAddress(host, port);
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
                    .childHandler(new ServerInitializer(statusGroup));

            final ChannelFuture channelFuture = bootstrap.bind(inetSocketAddress).sync();
            channel = channelFuture.channel();

            // await until channel in closed
            channel.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void close() {
        try {
            channel.close();
        } catch (NullPointerException e) {

        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


    public Channel getChannel() {
        return channel;
    }

    public void publish(Packet packet) {
        System.out.println(String.format("Publishing to:\t%s", statusGroup));
        statusGroup.writeAndFlush(packet);
    }
}
