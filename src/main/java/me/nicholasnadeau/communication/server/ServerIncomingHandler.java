package me.nicholasnadeau.communication.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.nicholasnadeau.robot.communication.packet.PacketProtos;

import java.util.Queue;

public class ServerIncomingHandler extends SimpleChannelInboundHandler<PacketProtos.Packet> {

    private Queue<PacketProtos.Packet> incomingQueue;

    public ServerIncomingHandler(Queue<PacketProtos.Packet> incomingQueue) {

        this.incomingQueue = incomingQueue;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PacketProtos.Packet msg) throws Exception {
        incomingQueue.add(msg);
    }
}
