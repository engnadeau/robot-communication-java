package me.nicholasnadeau.robot;

import me.nicholasnadeau.robot.RobotPacketProtos.RobotPacket;

public interface Communicator extends Runnable {
    void publish(RobotPacket packet);

    void close();
}
