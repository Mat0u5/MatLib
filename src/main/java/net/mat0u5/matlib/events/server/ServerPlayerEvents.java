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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.damagesource.DamageSource;
import org.jetbrains.annotations.Nullable;

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
	 * Fires when a player starts breaking a block.
	 */
	public static final Event<AttackBlock> ATTACK_BLOCK = EventFactory.create(AttackBlock.class,
			listeners -> (player, level, pos, direction) -> EventFactory.dispatchReturn(listeners, InteractionResult.PASS, listener -> listener.onAttackBlock(player, level, pos, direction))
	);

	/**
	 * Fires when a player right-clicks on a block.
	 */
	public static final Event<ClickBlock> CLICK_BLOCK = EventFactory.create(ClickBlock.class,
			listeners -> (player, level, stack, hand, blockHitResult) -> EventFactory.dispatchReturn(listeners, InteractionResult.PASS, listener -> listener.onClickBlock(player, level, stack, hand, blockHitResult))
	);

	/**
	 * Fires when a player dies.
	 */
	public static final Event<ItemUse> ITEM_USE = EventFactory.create(ItemUse.class,
			listeners -> (player, level, stack, hand) -> EventFactory.dispatchReturn(listeners, InteractionResult.PASS, listener -> listener.onItemUse(player, level, stack, hand))
	);

	/**
	 * Fires when a player interacts with an entity.
	 */
	public static final Event<ClickEntity> CLICK_ENTITY = EventFactory.create(ClickEntity.class,
			listeners -> (player, level, hand, entity, hitResult) -> EventFactory.dispatchReturn(listeners, InteractionResult.PASS, listener -> listener.onClickEntity(player, level, hand, entity, hitResult))
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
	public interface AttackBlock {
		InteractionResult onAttackBlock(ServerPlayer player, ServerLevel level, BlockPos pos, Direction direction);
	}

	@FunctionalInterface
	public interface ClickBlock {
		InteractionResult onClickBlock(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult blockHitResult);
	}

	@FunctionalInterface
	public interface ItemUse {
		InteractionResult onItemUse(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand);
	}

	@FunctionalInterface
	public interface ClickEntity {
		InteractionResult onClickEntity(ServerPlayer player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult);
	}
}
