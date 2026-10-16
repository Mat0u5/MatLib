package net.mat0u5.matlib.network;

import net.mat0u5.matlib.network.packets.simple.instances.SimpleEmptyPacket;
import net.mat0u5.matlib.network.packets.simple.instances.SimpleStringListPacket;
import net.mat0u5.matlib.network.packets.simple.instances.SimpleStringPacket;

public class MatLibSimplePackets {
	public static final SimpleStringListPacket SYNCED_CONFIG = new SimpleStringListPacket("SYNCED_CONFIG");
	public static final SimpleStringListPacket CONFIG_GUI_ENTRY = new SimpleStringListPacket("CONFIG_GUI_ENTRY");
	public static final SimpleStringListPacket CONFIG_GUI_UPDATE = new SimpleStringListPacket("CONFIG_GUI_UPDATE");
	public static final SimpleStringListPacket CONFIG_GUI_UPDATE_END = new SimpleStringListPacket("CONFIG_GUI_UPDATE_END");
	public static final SimpleEmptyPacket CLEAR_CONFIG = new SimpleEmptyPacket("CLEAR_CONFIG");
	public static final SimpleStringPacket LOAD_CLIENT_CONFIG = new SimpleStringPacket("LOAD_CLIENT_CONFIG");
	public static final SimpleStringPacket OPEN_CONFIG = new SimpleStringPacket("OPEN_CONFIG");
}
