package net.mat0u5.matlib.network.packets.simple.instances;

import net.mat0u5.matlib.network.packets.LongPayload;
import net.mat0u5.matlib.network.packets.simple.SimplePacket;

public class SimpleLongPacket extends SimplePacket<LongPayload, Long> {

    public SimpleLongPacket(String name) {
        super(name);
    }

    public LongPayload generatePayload(Long value) {
        if (value == null) return null;
        return new LongPayload(this.name, value);
    }
}
