package me.nicholasnadeau.robot.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import me.nicholasnadeau.robot.RobotPacketProtos.RobotPacket;

import java.util.Queue;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private ChannelGroup channelGroup;
    private Queue<RobotPacket> incomingQueue;

    public ServerInitializer(ChannelGroup channelGroup, Queue<RobotPacket> incomingQueue) {

        this.channelGroup = channelGroup;
        this.incomingQueue = incomingQueue;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        // incoming (top-down order)
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(RobotPacket.getDefaultInstance()));
        pipeline.addLast(new ServerRegistrarHandler(channelGroup));
        pipeline.addLast(new ServerIncomingHandler(incomingQueue));

        // outgoing (bottom-up order)
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
    }
}
