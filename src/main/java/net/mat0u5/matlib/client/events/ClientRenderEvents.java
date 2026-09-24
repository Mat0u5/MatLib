package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
//? if >= 1.21
import net.minecraft.client.DeltaTracker;

public class ClientRenderEvents {

	/**
	 * Fires when the GUI renders.
	 */
	public static final Event<RenderGui> RENDER_GUI = EventFactory.create(RenderGui.class,
			listeners -> (guiGraphics, deltaTracker) -> EventFactory.dispatch(listeners, listener -> listener.onRenderGui(guiGraphics, deltaTracker))
	).markLoud();

	/**
	 * Fires after the GUI renders.
	 */
	public static final Event<RenderGuiPost> RENDER_GUI_POST = EventFactory.create(RenderGuiPost.class,
			listeners -> (guiGraphics, deltaTracker) -> EventFactory.dispatch(listeners, listener -> listener.onPostRenderGui(guiGraphics, deltaTracker))
	).markLoud();


	@FunctionalInterface
	public interface RenderGui {
		//~ if >= 1.21 'float tickDelta' -> 'DeltaTracker deltaTracker' {
		void onRenderGui(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker);
		//~}
	}

	@FunctionalInterface
	public interface RenderGuiPost {
		//~ if >= 1.21 'float tickDelta' -> 'DeltaTracker deltaTracker' {
		void onPostRenderGui(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker);
		//~}
	}
}
