package me.nicholasnadeau.robot.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import me.nicholasnadeau.robot.communication.packet.PacketProtos.Packet;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private ChannelGroup statusGroup;

    public ServerInitializer(ChannelGroup statusGroup) {

        this.statusGroup = statusGroup;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        // logging
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));

        // incoming (top-down order)
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(Packet.getDefaultInstance()));
        pipeline.addLast(new ServerRegistrarHandler(statusGroup));

        // outgoing (bottom-up order)
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
    }
}
