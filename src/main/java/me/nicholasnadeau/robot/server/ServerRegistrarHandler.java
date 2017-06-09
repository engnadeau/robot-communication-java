package me.nicholasnadeau.robot.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;

import java.util.logging.Logger;

public class ServerRegistrarHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private ChannelGroup channelGroup;

    public ServerRegistrarHandler(ChannelGroup channelGroup) {

        this.channelGroup = channelGroup;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client connected:\t" + ctx.channel().remoteAddress());
        channelGroup.add(ctx.channel());

        super.channelActive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client disconnected:\t" + ctx.channel().remoteAddress());

        super.channelUnregistered(ctx);
    }
}
