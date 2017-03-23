package me.nicholasnadeau.communication;

import io.netty.channel.Channel;
import me.nicholasnadeau.robot.communication.packet.PacketProtos;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public abstract class AbstractCommunicator implements Communicator {
    protected InetSocketAddress inetSocketAddress;
    protected final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    protected Channel channel;
    protected Queue<PacketProtos.Packet> incomingQueue = new ConcurrentLinkedQueue<PacketProtos.Packet>();

    public int getPort() {
        return inetSocketAddress.getPort();
    }

    public String getHost() {
        return inetSocketAddress.getHostName();
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    public void setInetSocketAddress(InetSocketAddress inetSocketAddress) {
        this.inetSocketAddress = inetSocketAddress;
    }


    public Channel getChannel() {
        return channel;
    }


    public Queue<PacketProtos.Packet> getIncomingQueue() {
        return incomingQueue;
    }

    @Override
    public void close() {
        try {
            if (channel.isActive()) {
                channel.close();
            }
        } catch (NullPointerException e) {
            logger.severe(String.valueOf(e));
        }
    }
}
