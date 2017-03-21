package me.nicholasnadeau.robot.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.List;

public class ServerRegistrarHandler extends ChannelInboundHandlerAdapter {
    static private final Logger logger = LogManager.getLogger();

    private ChannelGroup statusGroup;

    public ServerRegistrarHandler(ChannelGroup statusGroup) {

        this.statusGroup = statusGroup;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client connected:\t{}", ctx.channel().remoteAddress());
        statusGroup.add(ctx.channel());

        super.channelActive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client disconnected:\t{}", ctx.channel().remoteAddress());

        super.channelUnregistered(ctx);
    }
}
