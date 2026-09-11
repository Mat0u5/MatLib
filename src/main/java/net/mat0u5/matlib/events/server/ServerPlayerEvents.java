package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.damagesource.DamageSource;

//? if <= 1.20.5 {
/*import net.minecraft.network.chat.Component;
*///?} else {
import net.minecraft.network.DisconnectionDetails;
//?}

public class ServerPlayerEvents {

	/**
	 * Fires when a player joins the server.
	 */
	public static final Event<Connect> CONNECT = EventFactory.create(Connect.class,
			listeners -> (connection, player) -> EventFactory.dispatch(listeners, listener -> listener.onConnect(connection, player))
	);

	/**
	 * Fires when a player leaves the server.
	 */
	public static final Event<Disconnect> DISCONNECT = EventFactory.create(Disconnect.class,
			listeners -> (details, player) -> EventFactory.dispatch(listeners, listener -> listener.onDisconnect(details, player))
	);

	/**
	 * Fires when a player dies.
	 */
	public static final Event<Death> DEATH = EventFactory.create(Death.class,
			listeners -> (player, source) -> EventFactory.dispatch(listeners, listener -> listener.onDeath(player, source))
	);

	/**
	 * Fires when a starts breaking a block.
	 */
	public static final Event<BlockAttack> BLOCK_ATTACK = EventFactory.create(BlockAttack.class,
			listeners -> (player, level, pos, direction) -> EventFactory.dispatchReturn(listeners, listener -> listener.onBlockAttack(player, level, pos, direction))
	);

	/**
	 * Fires when right-clicks on a block.
	 */
	public static final Event<BlockClick> BLOCK_CLICK = EventFactory.create(BlockClick.class,
			listeners -> (player, level, stack, hand, blockHitResult) -> EventFactory.dispatchReturn(listeners, listener -> listener.onClickBlock(player, level, stack, hand, blockHitResult))
	);

	/**
	 * Fires when a player dies.
	 */
	public static final Event<ItemUse> ITEM_USE = EventFactory.create(ItemUse.class,
			listeners -> (player, level, stack, hand) -> EventFactory.dispatchReturn(listeners, listener -> listener.onItemUse(player, level, stack, hand))
	);

	@FunctionalInterface
	public interface Connect {
		void onConnect(Connection connection, ServerPlayer player);
	}

	@FunctionalInterface
	public interface Disconnect {
		//? if <= 1.20.5 {
		/*void onDisconnect(Component reason, ServerPlayer player);
		*///?} else {
		void onDisconnect(DisconnectionDetails details, ServerPlayer player);
		//?}
	}

	@FunctionalInterface
	public interface Death {
		void onDeath(ServerPlayer player, DamageSource source);
	}

	@FunctionalInterface
	public interface BlockAttack {
		InteractionResult onBlockAttack(ServerPlayer player, ServerLevel level, BlockPos pos, Direction direction);
	}

	@FunctionalInterface
	public interface BlockClick {
		InteractionResult onClickBlock(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult blockHitResult);
	}

	@FunctionalInterface
	public interface ItemUse {
		InteractionResult onItemUse(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand);
	}
}
