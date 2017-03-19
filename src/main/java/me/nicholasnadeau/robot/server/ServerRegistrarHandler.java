package me.nicholasnadeau.robot.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;

import java.net.InetSocketAddress;
import java.util.List;

public class ServerRegistrarHandler extends ChannelInboundHandlerAdapter {

    private ChannelGroup statusGroup;

    public ServerRegistrarHandler(ChannelGroup statusGroup) {

        this.statusGroup = statusGroup;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(String.format("Client connected:\t%s", ctx.channel().remoteAddress()));
        statusGroup.add(ctx.channel());

        super.channelActive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(String.format("Client disconnected:\t%s", ctx.channel().remoteAddress()));

        super.channelUnregistered(ctx);
    }
}
