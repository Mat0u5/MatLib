package net.mat0u5.matlib.mixin;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.events.server.ServerLanguageEvents;
import net.mat0u5.matlib.utils.resource.ResourceHandler;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(Language.class)
public class LanguageMixin {

	@Inject(method = "loadDefault", at = @At("RETURN"), cancellable = true)
	private static void onServerLanguageLoad(CallbackInfoReturnable<Language> cir) {
		if (MatLib.hasClient()) return;
		Language vanilla = cir.getReturnValue();

		cir.setReturnValue(new Language() {
			private volatile Map<String, String> customTranslations = null;

			private Map<String, String> getCustomTranslations() {
				Map<String, String> translations = customTranslations;
				if (translations != null) return translations;
				if (!MatLib.isRegistered()) return Map.of();
				translations = new HashMap<>();
				List<String> fileNames = ServerLanguageEvents.LOAD_LANG_FILES.invoker().getPaths();
				for (String fileName : fileNames) {
					try (InputStream is = ResourceHandler.openBundledResource(fileName)) {
						if (is != null) {
							Language.loadFromJson(is, translations::put);
						}
						else {
							MatLib.LOGGER.error("Server-side translation file not found: {}", fileName);
						}
					} catch (Exception e) {
						MatLib.LOGGER.error("Failed to load server-side translation: {}", fileName, e);
					}
				}
				customTranslations = translations;
				return translations;
			}

			@Override
			public String getOrDefault(String key, String fallback) {
				Map<String, String> translations = getCustomTranslations();
				if (translations.containsKey(key)) {
					return translations.get(key);
				}
				return vanilla.getOrDefault(key, fallback);
			}

			@Override
			public boolean has(String key) {
				return getCustomTranslations().containsKey(key) || vanilla.has(key);
			}

			@Override
			public boolean isDefaultRightToLeft() {
				return vanilla.isDefaultRightToLeft();
			}

			@Override
			public FormattedCharSequence getVisualOrder(FormattedText text) {
				return vanilla.getVisualOrder(text);
			}
		});
	}
}