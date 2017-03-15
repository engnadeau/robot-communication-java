package me.nicholasnadeau.robot.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import me.nicholasnadeau.robot.communication.packet.PacketProtos.Packet;

public class ServerInitializer extends ChannelInitializer<DatagramChannel> {
    private static final int MAX_FRAME_LENGTH = 1024;
    private static final int LENGTH_FIELD_LENGTH = 4;

    @Override
    protected void initChannel(DatagramChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        // logging
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));

        // decoders
//        pipeline.addLast("lengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(
//                MAX_FRAME_LENGTH, 0, LENGTH_FIELD_LENGTH, 0, LENGTH_FIELD_LENGTH));
//        pipeline.addLast("protobufDecoder", new ProtobufDecoder(Packet.getDefaultInstance()));
//
//        // encoders
//        pipeline.addLast("lengthFieldPrepender", new LengthFieldPrepender(LENGTH_FIELD_LENGTH));
//        pipeline.addLast("protobufEncoder", new ProtobufEncoder());
    }
}
