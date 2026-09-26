package net.mat0u5.matlib.network.packets.simple.instances;

import net.mat0u5.matlib.network.packets.DoublePayload;
import net.mat0u5.matlib.network.packets.simple.SimplePacket;

public class SimpleDoublePacket extends SimplePacket<DoublePayload, Double> {

    public SimpleDoublePacket(String name) {
        super(name);
    }

    public DoublePayload generatePayload(Double value) {
        if (value == null) return null;
        return new DoublePayload(this.name, value);
    }
}
