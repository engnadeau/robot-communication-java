package me.nicholasnadeau.communication;

import me.nicholasnadeau.robot.communication.packet.PacketProtos;

public interface Communicator extends Runnable {
    void publish(PacketProtos.Packet packet);

    void close();
}
