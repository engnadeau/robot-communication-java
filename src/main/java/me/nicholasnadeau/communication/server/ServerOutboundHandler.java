package me.nicholasnadeau.communication.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.net.InetSocketAddress;
import java.util.List;

public class ServerOutboundHandler extends MessageToMessageEncoder<ByteBuf> {
    private List<InetSocketAddress> clientAddresses;

    public ServerOutboundHandler(List<InetSocketAddress> clientAddresses) {
        this.clientAddresses = clientAddresses;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    }
}
