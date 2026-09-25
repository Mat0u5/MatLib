package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.events.OptionalEventReturn;
import net.mat0u5.matlib.events.common.CommonPlayerEvents;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.jetbrains.annotations.NotNull;

//? if <= 1.20 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.world.entity.player.PlayerSkin;
//?}

public class ClientPlayerEvents extends CommonPlayerEvents {

	/**
	 * Fires when a player fetches their skin.
	 */
	public static final Event<GetSkin> GET_SKIN = EventFactory.createClient(GetSkin.class,
			listeners -> (playerInfo, originalReturn) -> EventFactory.dispatchOptionalReturn(listeners, listener -> listener.getSkin(playerInfo, originalReturn))
	).markLoud();

	@FunctionalInterface
	public interface GetSkin {
		//? if <= 1.20 {
		/*@NotNull OptionalEventReturn<Identifier> getSkin(PlayerInfo playerInfo, Identifier originalReturn);
		*///?} else {
		@NotNull OptionalEventReturn<PlayerSkin> getSkin(PlayerInfo playerInfo, PlayerSkin originalReturn);
		//?}
	}
}
