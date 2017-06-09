package me.nicholasnadeau.robot.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import me.nicholasnadeau.robot.RobotPacketProtos.RobotPacket;

import java.util.Queue;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private Queue<RobotPacket> incomingQueue;

    public ClientInitializer(Queue<RobotPacket> incomingQueue) {
        this.incomingQueue = incomingQueue;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // incoming (top-down order)
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(RobotPacket.getDefaultInstance()));
        pipeline.addLast(new ClientIncomingHandler(incomingQueue));

        // outgoing (bottom-up order)
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
    }
}
