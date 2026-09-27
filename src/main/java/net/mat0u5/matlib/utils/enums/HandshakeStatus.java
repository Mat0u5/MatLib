package net.mat0u5.matlib.utils.enums;

import java.util.ArrayList;
import java.util.List;

public class HandshakeStatus {
    private volatile boolean waiting = true;
    private volatile List<String> receivedMods = new ArrayList<>();
    public boolean isWaiting() {
        return waiting;
    }

    public boolean hasReceived(String modId) {
        return receivedMods.contains(modId);
    }

    public void setReceivedMods(List<String> modIds) {
        receivedMods = modIds;
        waiting = false;
    }
}
