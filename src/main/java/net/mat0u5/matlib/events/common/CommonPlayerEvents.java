package net.mat0u5.matlib.events.common;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class CommonPlayerEvents {

	/**
	 * Fires when a player attacks an entity.
	 */
	public static final Event<AttackEntity> ATTACK_ENTITY = EventFactory.createCommon(AttackEntity.class,
			listeners -> (player, entity) -> EventFactory.dispatchReturn(listeners, InteractionResult.PASS, listener -> listener.onAttackEntity(player, entity))
	);

	/**
	 * Fires when a player's inventory is updated.
	 */
	public static final Event<UpdateInventory> UPDATE_INVENTORY = EventFactory.createCommon(UpdateInventory.class,
			listeners -> (player, inventory) -> EventFactory.dispatch(listeners, listener -> listener.onUpdateInventory(player, inventory))
	);

	@FunctionalInterface
	public interface AttackEntity {
		InteractionResult onAttackEntity(Player player, Entity entity);
	}

	@FunctionalInterface
	public interface UpdateInventory {
		void onUpdateInventory(Player player, Inventory inventory);
	}
}
