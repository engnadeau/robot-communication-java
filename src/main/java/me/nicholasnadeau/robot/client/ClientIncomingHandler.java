package me.nicholasnadeau.robot.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.nicholasnadeau.robot.RobotPacketProtos.RobotPacket;

import java.util.Queue;

public class ClientIncomingHandler extends SimpleChannelInboundHandler<RobotPacket> {
    private Queue<RobotPacket> incomingQueue;

    public ClientIncomingHandler(Queue<RobotPacket> incomingQueue) {
        this.incomingQueue = incomingQueue;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RobotPacket msg) throws Exception {
        incomingQueue.add(msg);
    }
}
