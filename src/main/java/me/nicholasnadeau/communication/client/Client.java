package me.nicholasnadeau.communication.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.nicholasnadeau.communication.AbstractCommunicator;
import me.nicholasnadeau.robot.communication.packet.PacketProtos;

import java.net.InetSocketAddress;

public class Client extends AbstractCommunicator {
    private EventLoopGroup eventLoopGroup;

    public Client(String host, int port) {
        inetSocketAddress = new InetSocketAddress(host, port);
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
            channel = b.connect(inetSocketAddress).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.severe(String.valueOf(e));
        } finally {
            close();
        }
    }

    @Override
    public void publish(PacketProtos.Packet packet) {
        logger.fine("Publishing to:\t" + channel);
        channel.writeAndFlush(packet);
    }

    @Override
    public void close() {
        super.close();
        eventLoopGroup.shutdownGracefully();
    }
}