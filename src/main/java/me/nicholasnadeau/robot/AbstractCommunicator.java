package me.nicholasnadeau.robot;

import io.netty.channel.Channel;
import me.nicholasnadeau.robot.RobotPacketProtos.RobotPacket;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public abstract class AbstractCommunicator implements Communicator {
    protected InetSocketAddress inetSocketAddress;
    protected final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    protected Channel channel;
    protected Queue<RobotPacket> incomingQueue = new ConcurrentLinkedQueue<RobotPacket>();

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


    public Queue<RobotPacket> getIncomingQueue() {
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
