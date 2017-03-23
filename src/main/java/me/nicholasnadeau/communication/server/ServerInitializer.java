package me.nicholasnadeau.communication.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import me.nicholasnadeau.robot.communication.packet.PacketProtos.Packet;

import java.util.Queue;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private ChannelGroup channelGroup;
    private Queue<Packet> incomingQueue;

    public ServerInitializer(ChannelGroup channelGroup, Queue<Packet> incomingQueue) {

        this.channelGroup = channelGroup;
        this.incomingQueue = incomingQueue;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        // incoming (top-down order)
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(Packet.getDefaultInstance()));
        pipeline.addLast(new ServerRegistrarHandler(channelGroup));
        pipeline.addLast(new ServerIncomingHandler(incomingQueue));

        // outgoing (bottom-up order)
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
    }
}
