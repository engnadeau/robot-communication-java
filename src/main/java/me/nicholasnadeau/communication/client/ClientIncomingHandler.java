package me.nicholasnadeau.communication.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.nicholasnadeau.robot.communication.packet.PacketProtos;

import java.util.Queue;

public class ClientIncomingHandler extends SimpleChannelInboundHandler<PacketProtos.Packet> {
    private Queue<PacketProtos.Packet> incomingQueue;

    public ClientIncomingHandler(Queue<PacketProtos.Packet> incomingQueue) {
        this.incomingQueue = incomingQueue;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PacketProtos.Packet msg) throws Exception {
        incomingQueue.add(msg);
    }
}
